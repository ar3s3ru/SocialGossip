package socialgossip.server.core.gateways;

/**
 * Exception raised because of a data gateway error, typically database error.
 */
public class GatewayException extends Exception {
    public GatewayException(final String message) {
        super("data gateway exception: " + message);
    }
}
