package socialgossip.server.core.entities.password;

import java.util.Objects;

/**
 * Represents an encryption schema, or algorithm, that generates {@link EncryptedPassword}
 * from plain-text passwords.
 *
 * @param <T> is the type of hash and salt parameters, typically {@link String}.
 */
public abstract class EncryptedSchema<T> {
    private final PasswordValidator validator;

    protected EncryptedSchema(final PasswordValidator validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    /**
     * Creates a new {@link EncryptedPassword} from a plain-text password.
     * @param password is the plain-text password.
     * @return an {@link EncryptedPassword} using the {@link EncryptedSchema} of the implementor class.
     */
    public final EncryptedPassword<T> from(final String password)
        throws InvalidPasswordException {
        if (password.isEmpty()) {
            throw new InvalidPasswordException(password, "empty password");
        }
        validator.validate(password);
        return this.encrypt(password);
    }

    protected abstract EncryptedPassword<T> encrypt(String password);
}
