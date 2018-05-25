package socialgossip.server.entrypoints.tcp;

import java.util.Objects;

public abstract class AbstractController<InputType> implements Controller<InputType> {
    public abstract String OPCODE();

    public AbstractController(final TCPServer tcpServer) {
        Objects.requireNonNull(tcpServer).registerController(OPCODE(), this);
    }
}
