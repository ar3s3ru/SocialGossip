package socialgossip.server.core.gateways.session;

public class SessionAlreadyExistsException extends Exception {
    public SessionAlreadyExistsException(String sessionToken, String username) {
        super("User " + username + " tried to add a new Session with token " + sessionToken);
    }
}
