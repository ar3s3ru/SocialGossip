package socialgossip.server.security;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;

/**
 * Simple password validator.
 * Passwords should not be null, or empty, and have at least 8 characters.
 */
public class SimplePasswordValidator implements PasswordValidator {
    @Override
    public void validate(final String password) throws InvalidPasswordException {
        if (password == null) {
            throw new InvalidPasswordException(password, "password is null");
        } else if (password.isEmpty()) {
            throw new InvalidPasswordException(password, "empty password");
        } else if (password.length() < 8) {
            throw new InvalidPasswordException(password, "should be at least 8 characters long");
        }
    }
}
