package socialgossip.server.core.entities.auth;

/**
 * Exception raised when trying to access a {@link ProtectedResource} with
 * an unauthorized {@link Permission}.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(final String token, final String message) {
        super("Permission token " + token + " unauthorized: " + message);
    }
}
