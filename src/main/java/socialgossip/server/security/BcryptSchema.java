package socialgossip.server.security;

import org.mindrot.jbcrypt.BCrypt;
import socialgossip.server.core.entities.password.EncryptedPassword;
import socialgossip.server.core.entities.password.EncryptedSchema;
import socialgossip.server.core.entities.password.PasswordValidator;

import java.util.Objects;

/**
 * {@link EncryptedSchema} implementation for the {@link BCrypt} encryption algorithm.
 */
public class BcryptSchema extends EncryptedSchema<String> {

    public BcryptSchema(final PasswordValidator validator) {
        super(validator);
    }

    /**
     * Returns a new encrypted {@link BcryptPassword} from a plain-text password.
     * @param password is the plain-text password.
     * @throws NullPointerException if the input password is null.
     * @return a new encrypted {@link BcryptPassword} password.
     */
    @Override
    protected EncryptedPassword<String> encrypt(final String password) {
        final String notNullPassword = Objects.requireNonNull(password);
        final String salt = BCrypt.gensalt();
        final String hash = BCrypt.hashpw(notNullPassword, salt);
        return new BcryptPassword(hash, salt);
    }

    /**
     * Encrypted {@link socialgossip.server.core.entities.password.Password}
     * using the {@link BCrypt} encryption schema.
     */
    public final static class BcryptPassword extends EncryptedPassword<String> {

        private BcryptPassword(String hash, String salt) {
            super(hash, salt);
        }

        /**
         * Creates a new {@link BcryptPassword} from hash and salt parameters.
         * Executes a pre-validation to ensure it conforms to BCrypt algorithm.
         *
         * @param hash is the encrypted password hash.
         * @param salt is the encrypted password salt.
         * @return a {@link BcryptPassword} instance with hash and salt.
         */
        public static BcryptPassword from(String hash, String salt) {
            // TODO(ar3s3ru): add a validation step.
            return new BcryptPassword(hash, salt);
        }

        @Override
        public boolean verify(final String password) {
            final String notNullPassword = Objects.requireNonNull(password);
            return BCrypt.checkpw(notNullPassword, this.getHash());
        }
    }
}
