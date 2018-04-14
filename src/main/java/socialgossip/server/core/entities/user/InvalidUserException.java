package socialgossip.server.core.entities.user;

/**
 * Exception raised when trying to create an invalid {@link User} object.
 */
public class InvalidUserException extends Exception {
    /**
     * Creates a new {@link InvalidUserException} explaining why the {@link User}
     * is invalid.
     * @param reason explains why the {@link User} is invalid.
     */
    InvalidUserException(final String reason) {
        super("invalid user id specified: " + reason);
    }
}
