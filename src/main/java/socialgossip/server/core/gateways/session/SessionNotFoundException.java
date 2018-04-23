package socialgossip.server.core.gateways.session;

/**
 * Exception raised when trying to retrieve a {@link socialgossip.server.core.entities.session.Session}
 * using a token that doesn't exist.
 */
public class SessionNotFoundException extends Exception {
    public SessionNotFoundException(final String token) {
        super("Session " + token + " not found");
    }
}
