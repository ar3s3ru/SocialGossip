package socialgossip.server.configuration.security;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.configuration.application.ApplicationScope;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.security.BcryptSchema;
import socialgossip.server.security.SimplePasswordValidator;

import javax.inject.Singleton;

@Module
public class BcryptModule {
    @Provides
    @ApplicationScope
    static PasswordValidator providePasswordValidator() {
        return new SimplePasswordValidator();
    }

    @Provides
    @ApplicationScope
    static EncryptionSchema<?> provideEncryptionSchema(final PasswordValidator validator) {
        return new BcryptSchema(validator);
    }
}
