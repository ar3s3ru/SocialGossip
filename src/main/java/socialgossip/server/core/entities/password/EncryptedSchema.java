package socialgossip.server.core.entities.password;

/**
 * Represents an encryption schema, or algorithm, that generates {@link EncryptedPassword}
 * from plain-text passwords.
 *
 * @param <T> is the type of hash and salt parameters, typically {@link String}.
 */
public interface EncryptedSchema<T> {
    /**
     * Creates a new {@link EncryptedPassword} from a plain-text password.
     * @param password is the plain-text password.
     * @return an {@link EncryptedPassword} using the {@link EncryptedSchema} of the implementor class.
     */
    EncryptedPassword<T> from(String password);
}
