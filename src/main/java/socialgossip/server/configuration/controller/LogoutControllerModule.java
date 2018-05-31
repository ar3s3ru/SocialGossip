package socialgossip.server.configuration.controller;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.entrypoints.tcp.authorized.logout.LogoutController;
import socialgossip.server.presentation.logout.LogoutPresenter;
import socialgossip.server.usecases.logout.LogoutErrors;
import socialgossip.server.usecases.logout.LogoutUseCase;

@Module
public class LogoutControllerModule {
    @Provides
    @ControllerScope static LogoutController provideController(
            final LogoutPresenter presenter,
            final LogoutUseCase<Boolean, LogoutErrors> interactor
    ) {
        return new LogoutController(presenter, interactor);
    }
}
