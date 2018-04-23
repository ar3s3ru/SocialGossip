package socialgossip.server.core.gateways.user;

/**
 * Exception raised when an {@link socialgossip.server.core.entities.user.User}
 * with a specified {@code username} has not been found.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(final String username) {
        super("User " + username + " not found");
    }
}
