package socialgossip.server.entrypoints.tcp;

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

public final class TCPServer implements Runnable {
    private final Map<String, Controller> controllersMap = new HashMap<>();
    private final AtomicInteger           requestCounter = new AtomicInteger();

    private final String serverName;
    private final int serverPort;
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
            controllersMap.put(validatedOpcode(opcode), Objects.requireNonNull(controller));
        }
    }

    public void stopServer() {
        keepRunning = false;
    }

    @Override
    public void run() {
        try (final ServerSocket welcomeSocket = new ServerSocket(serverPort)) {
            while (keepRunning) {
                try {
                    final Socket acceptedSocket = welcomeSocket.accept();
                    acceptedSocket.setKeepAlive(true);
                    final Future future = executor.submit(
                            new TCPHandler(
                                    generateRequestId(serverName, requestCounter.addAndGet(1)),
                                    acceptedSocket,
                                    controllersMap
                            )
                    );
                    future.get();
                } catch (IOException e) {
                    // TODO: can't accept connection
                } catch (InterruptedException | ExecutionException e) {

                }
            }
        } catch (IOException e) {
            // TODO: can't open connection
            throw new RuntimeException(e);
        }
    }
}
