package socialgossip.server.core.gateways.session;

/**
 * Exception raised when trying to insert a new {@link socialgossip.server.core.entities.session.Session}
 * to the persistence storage, but one with the same token already exists.
 */
public class SessionAlreadyExistsException extends Exception {
    public SessionAlreadyExistsException(String sessionToken, String username) {
        super("User " + username + " tried to insert a new Session with token " + sessionToken);
    }
}
