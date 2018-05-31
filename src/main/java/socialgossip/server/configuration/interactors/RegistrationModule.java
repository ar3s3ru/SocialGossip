package socialgossip.server.configuration.interactors;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.usecases.registration.RegistrationInteractor;


@Module(includes = InteractorsModule.class)
public class RegistrationModule {
    @Provides
    @UseCaseScope
    static RegistrationInteractor provideRegistrationInteractor(
            final UserInserter inserter, final EncryptionSchema<?> schema
    ) {
        return new RegistrationInteractor(inserter, schema);
    }
}