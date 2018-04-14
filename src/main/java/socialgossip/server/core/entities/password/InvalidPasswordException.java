package socialgossip.server.core.entities.password;

/**
 * Exception raised by a {@link PasswordValidator} when the password has been found invalid.
 */
public class InvalidPasswordException extends Exception {
    /**
     * Creates a new {@link InvalidPasswordException} with the invalid password and a message
     * explaining why the password is invalid.
     * @param password is the invalid password.
     * @param reason explains why the password is invalid.
     */
    public InvalidPasswordException(final String password, final String reason) {
        super("Invalid password \"" + password + "\": " + reason);
    }
}
