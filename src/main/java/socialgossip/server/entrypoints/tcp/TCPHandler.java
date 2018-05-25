package socialgossip.server.entrypoints.tcp;

import socialgossip.server.logging.AppLogger;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

final class TCPHandler implements Runnable {
    private static final Logger LOG = Logger.getLogger(TCPHandler.class.getName());

    private final Map<String, Controller> controllersMap;
    private final Socket socket;
    private final String requestId;

    public TCPHandler(final String requestId, final Socket socket, final Map<String, Controller> controllersMap) {
        this.controllersMap = Objects.requireNonNull(controllersMap);
        this.socket         = Objects.requireNonNull(socket);
        this.requestId      = requestId;
    }

    @Override
    public void run() {
        AppLogger.fine(LOG, () -> requestId, () -> "TCP handler started");
        try (
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            while (!socket.isClosed()) {
                try {
                    final String opcode = reader.readLine();
                    AppLogger.fine(LOG, () -> requestId, () -> "requested opcode: " + opcode);
                    final Controller controller = controllersMap.get(opcode);
                    if (Objects.isNull(controller)) {
                        // TODO: write an error message to the user
                        AppLogger.warn(LOG, () -> requestId, () -> "opcode not found!");
                        writer.write("invalid op requested");
                        continue;
                    }
                    final String request = reader.readLine();
                    controller.handle(requestId, request, writer);
                } finally {
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (IOException e) {
            AppLogger.error(LOG, () -> requestId, () -> "error while handling request: " + e);
        }
    }
}
