package socialgossip.server.entrypoints.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;

final class TCPHandler implements Runnable {
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
        try (
                final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            while (!socket.isClosed()) {
                final String opcode  = reader.readLine();
                final Controller controller = controllersMap.get(opcode);
                if (Objects.isNull(controller)) {
                    // TODO: write an error message to the user
                    writer.write("invalid op requested");
                }
                final String request = reader.readLine();
                controller.handle(requestId, request, writer);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            // TODO: reading or writing went wrong
            throw new RuntimeException(e);
        }
    }
}
