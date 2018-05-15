package socialgossip.server.core.gateways.user;

/**
 * Exception raised when trying to insert an {@link socialgossip.server.core.entities.user.User}
 * to the persistence layer wit an already-existing user id.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(final String id) {
        super("user " + id + " already exists");
    }
}
