package socialgossip.server.core.entities.password;

/**
 * Object that executes validation on a plain-text password.
 */
public interface PasswordValidator {
    /**
     * Validates an input plain-text password.
     * If not valid, raises an {@link InvalidPasswordException}.
     * @param password is the plain-text password to validate.
     * @throws InvalidPasswordException if the password is invalid, explaining why.
     */
    void validate(String password) throws InvalidPasswordException;
}
