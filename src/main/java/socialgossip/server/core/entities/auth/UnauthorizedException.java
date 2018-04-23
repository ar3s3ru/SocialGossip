package socialgossip.server.core.entities.auth;

import socialgossip.server.core.entities.session.Session;

/**
 * Exception raised when trying to access a {@link ProtectedResource} with
 * an unauthorized {@link Permission}.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(final Session session, final String message) {
        super("Session " + session.getToken() + " unauthorized: " + message);
    }
}
