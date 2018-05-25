package socialgossip.server.entrypoints.tcp;

import socialgossip.server.logging.AppLogger;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

final class TCPHandler implements Runnable {
    private static final Logger LOG = Logger.getLogger(TCPHandler.class.getName());

    private final Map<String, Controller> controllersMap;
    private final Socket socket;
    private final String connectionId;

    private final AtomicInteger requestCounter = new AtomicInteger();

    public static String generateRequestId(final String connectionId, final int requestCount) {
        return connectionId + "/" + requestCount;
    }

    public TCPHandler(final String connectionId, final Socket socket, final Map<String, Controller> controllersMap) {
        this.controllersMap = Objects.requireNonNull(controllersMap);
        this.socket         = Objects.requireNonNull(socket);
        this.connectionId   = connectionId;
    }

    @Override
    public void run() {
        AppLogger.fine(LOG, () -> connectionId, () -> "TCP handler started");
        try (
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            while (!socket.isClosed()) {
                final String requestId = generateRequestId(connectionId, requestCounter.addAndGet(1));
                try {
                    final String opcode = reader.readLine();
                    AppLogger.fine(LOG, () -> requestId, () -> "requested opcode: " + opcode);
                    if (Objects.isNull(opcode)) {
                        // Close connection on null opcode (probably was EOF).
                        // Connection will be closed by try-with-resource.
                        break;
                    }

                    final Controller controller = controllersMap.get(opcode);
                    if (Objects.isNull(controller)) {
                        AppLogger.warn(LOG, () -> requestId, () -> "opcode not found!");
                        writer.write("invalid op requested");
                        continue;
                    }

                    final String body = reader.readLine();
                    // TODO: add recycling mechanism
                    final TCPRequest request = new TCPRequest(requestId, body, socket.getInetAddress());
                    controller.handle(request, writer);
                } finally {
                    if (!socket.isClosed()) {
                        writer.newLine();
                        writer.flush();
                    }
                }
            }
        } catch (IOException e) {
            AppLogger.error(LOG, () -> connectionId, () -> "error while handling connection: " + e);
        }
        AppLogger.fine(LOG, () -> connectionId, () -> "connection closed");
    }
}
