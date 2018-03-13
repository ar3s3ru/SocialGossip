package socialgossip.server.core.gateways;

public abstract class GatewayException extends Exception {
    public GatewayException(final String message) {
        super("gateway exception: " + message);
    }
}
