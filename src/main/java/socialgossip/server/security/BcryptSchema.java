package socialgossip.server.security;

import socialgossip.server.core.entities.password.EncryptedPassword;
import socialgossip.server.core.entities.password.EncryptedSchema;

public class BcryptSchema implements EncryptedSchema<byte[]> {

    @Override
    public EncryptedPassword<byte[]> from(String password) {
        throw new RuntimeException("implement me!");
    }

    public final static class BcryptPassword extends EncryptedPassword<byte[]> {

        BcryptPassword(byte[] hash, byte[] salt) {
            super(hash, salt);
        }

        /**
         * Creates a new {@link BcryptPassword} from hash and salt parameters.
         * Executes a prevalidation to ensure it conforms to Bcrypt algorithm.
         *
         * @param hash is the encrypted password hash.
         * @param salt is the encrypted password salt.
         * @return a {@link BcryptPassword} instance with hash and salt.
         */
        public static BcryptPassword from(byte[] hash, byte[] salt) {
            throw new RuntimeException("implement me!");
        }

        @Override
        public boolean verify(String password) {
            return false;
        }
    }
}
