package socialgossip.server.core.entities.auth;

import socialgossip.server.core.entities.session.Session;

public class UnauthorizedException extends Exception {
    public UnauthorizedException(final Session session, final String message) {
        super("Session " + session.getToken() + " unauthorized: " + message);
    }
}
