package socialgossip.server.configuration.presentation;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.presentation.login.LoginPresenter;
import socialgossip.server.presentation.logout.LogoutPresenter;
import socialgossip.server.presentation.registration.RegistrationPresenter;

@Module
public class PresentersModule {
    @Provides static RegistrationPresenter provideRegistrationPresenter() {
        return new RegistrationPresenter();
    }

    @Provides static LoginPresenter provideLoginPresenter() {
        return new LoginPresenter();
    }

    @Provides static LogoutPresenter provideLogoutPresenter() {
        return new LogoutPresenter();
    }
}
