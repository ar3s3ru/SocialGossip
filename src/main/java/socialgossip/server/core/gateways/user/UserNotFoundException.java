package socialgossip.server.core.gateways.user;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(final String username) {
        super("User " + username + " not found");
    }
}
