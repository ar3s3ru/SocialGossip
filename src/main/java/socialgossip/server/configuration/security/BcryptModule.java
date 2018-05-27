package socialgossip.server.configuration.security;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.security.BcryptSchema;
import socialgossip.server.security.SimplePasswordValidator;

@Module
public class BcryptModule {
    @Provides
    public static PasswordValidator providePasswordValidator() {
        return new SimplePasswordValidator();
    }

    @Provides
    public EncryptionSchema<?> provideEncryptionSchema(final PasswordValidator validator) {
        return new BcryptSchema(validator);
    }
}
