package socialgossip.server.core.entities.password;

/**
 * Represents a password, encrypted or plain-text.
 */
public interface Password {
    /**
     * Given an input plain-text password, verifies that it's the same password.
     * @param password is the plain-text password.
     * @return true if the passwords match, false otherwise.
     */
    boolean verify(String password);
}