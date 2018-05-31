package socialgossip.server.configuration.security;

import dagger.Subcomponent;
import socialgossip.server.configuration.application.ApplicationScope;
import socialgossip.server.configuration.dataproviders.DataproviderComponent;
import socialgossip.server.configuration.dataproviders.inmemory.InMemoryModule;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.PasswordValidator;

@Subcomponent(modules = BcryptModule.class)
@ApplicationScope
public interface SecurityComponent {
    EncryptionSchema<?> encryptionSchema();
    PasswordValidator   passwordValidator();

    DataproviderComponent attachDataproviderComponent(InMemoryModule module);
}
