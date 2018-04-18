package socialgossip.server.core.gateways.session;

public class SessionNotFoundException extends Exception {
    public SessionNotFoundException(final String token) {
        super("Session " + token + " not found");
    }
}
