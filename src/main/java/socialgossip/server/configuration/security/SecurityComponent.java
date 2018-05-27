package socialgossip.server.configuration.security;

import dagger.Component;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.PasswordValidator;

@Component(modules = BcryptModule.class)
public interface SecurityComponent {
    EncryptionSchema<?> encryptionSchema();
    PasswordValidator   passwordValidator();
}
