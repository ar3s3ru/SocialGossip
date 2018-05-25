package socialgossip.server.entrypoints.tcp;

import socialgossip.server.logging.AppLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class TCPServer implements Runnable {
    private static final Logger LOG = Logger.getLogger(TCPServer.class.getName());

    private final Map<String, Controller> controllersMap = new HashMap<>();
    private final AtomicInteger           requestCounter = new AtomicInteger();

    private final String             serverName;
    private final int                serverPort;
    private final ThreadPoolExecutor executor;

    private volatile boolean keepRunning;

    private static String generateRequestId(final String name, final int count) {
        return name + "/" + count;
    }

    public TCPServer(final String serverName, final int port, final ThreadPoolExecutor executor) {
        serverPort  = port;
        keepRunning = true;
        this.serverName = Objects.requireNonNull(serverName);
        this.executor   = Objects.requireNonNull(executor);
    }

    public String validatedOpcode(final String opcode) {
        Objects.requireNonNull(opcode);
        if (opcode.length() == 0) {
            throw new IllegalArgumentException("opcode can't be empty string");
        }
        if (controllersMap.containsKey(opcode)) {
            throw new IllegalArgumentException("opcode already present");
        }
        return opcode;
    }

    public void registerController(final String opcode, final Controller controller) {
        synchronized (controllersMap) {
            final String code = validatedOpcode(opcode);
            AppLogger.fine(LOG, () -> serverName, () -> "registering opcode: " + code);
            controllersMap.put(code, Objects.requireNonNull(controller));
        }
    }

    public void stopServer() {
        keepRunning = false;
    }

    @Override
    public void run() {
        AppLogger.info(LOG, () -> serverName, () -> "listening on port " + serverPort);
        try (final ServerSocket welcomeSocket = new ServerSocket(serverPort)) {
            while (keepRunning) {
                // Pre-generate next request id
                final String requestId = generateRequestId(serverName, requestCounter.addAndGet(1));
                try {
                    final Socket acceptedSocket = welcomeSocket.accept();
                    acceptedSocket.setKeepAlive(true);
                    final Future future = executor.submit(
                            new TCPHandler(requestId, acceptedSocket, controllersMap)
                    );
                    future.get();
                } catch (IOException e) {
                    AppLogger.warn(LOG, () -> requestId, () -> "can't accept connection: " + e);
                } catch (InterruptedException | ExecutionException e) {
                    AppLogger.warn(LOG, () -> requestId, () -> "request interrupted: " + e);
                }
            }
        } catch (IOException e) {
            // TODO: can't open connection
            throw new RuntimeException(e);
        }
    }
}
