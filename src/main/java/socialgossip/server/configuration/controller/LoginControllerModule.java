package socialgossip.server.configuration.controller;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.entrypoints.tcp.login.LoginController;
import socialgossip.server.presentation.login.LoginPresenter;
import socialgossip.server.usecases.login.LoginErrors;
import socialgossip.server.usecases.login.LoginOutput;
import socialgossip.server.usecases.login.LoginUseCase;

@Module
public class LoginControllerModule {
    @Provides
    @ControllerScope static LoginController provideLoginController(
            final LoginPresenter presenter,
            final LoginUseCase<LoginOutput, LoginErrors> interactor
    ) {
        return new LoginController(presenter, interactor);
    }
}
