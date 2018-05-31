package socialgossip.server.configuration.presentation;

import dagger.Subcomponent;
import socialgossip.server.presentation.login.LoginPresenter;
import socialgossip.server.presentation.logout.LogoutPresenter;
import socialgossip.server.presentation.registration.RegistrationPresenter;

@Subcomponent(modules = PresentersModule.class)
public interface PresentationComponent {
    RegistrationPresenter registrationPresenter();
    LoginPresenter        loginPresenter();
    LogoutPresenter       logoutPresenter();
}
