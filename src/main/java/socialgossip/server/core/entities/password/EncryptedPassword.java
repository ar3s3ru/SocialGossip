package socialgossip.server.core.entities.password;

import java.util.Objects;

/**
 * Represents a {@link Password} encrypted with an {@link EncryptionSchema}.
 * @param <T> is the type of hash and salt, typically {@link String} or byte[].
 */
public abstract class EncryptedPassword<T> implements Password {
    private final T hash;
    private final T salt;

    /**
     * Creates a new EncryptedPassword.
     * @param hash is the encrypted password hash.
     * @param salt is the encrypted password salt.
     * @throws NullPointerException if hash or salt are {@literal null}.
     */
    protected EncryptedPassword(T hash, T salt) {
        this.hash = Objects.requireNonNull(hash);
        this.salt = Objects.requireNonNull(salt);
    }

    public T getHash() {
        return hash;
    }

    public T getSalt() {
        return salt;
    }
}
