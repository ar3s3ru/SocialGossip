package socialgossip.server.configuration.controller;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.entrypoints.tcp.registration.RegistrationController;
import socialgossip.server.presentation.registration.RegistrationPresenter;
import socialgossip.server.usecases.registration.RegistrationUseCase;

@Module
public class RegistrationControllerModule {
    @Provides
    @ControllerScope static RegistrationController provideController(
            final RegistrationPresenter presenter,
            final RegistrationUseCase interactor
    ) {
        return new RegistrationController(presenter, interactor);
    }
}
