package socialgossip.server.configuration.interactors;

import dagger.Subcomponent;
import socialgossip.server.usecases.login.LoginUseCase;
import socialgossip.server.usecases.logout.LogoutUseCase;
import socialgossip.server.usecases.registration.RegistrationUseCase;

@Subcomponent(modules = {
        RegistrationModule.class,
        LoginModule.class,
        LogoutModule.class
})
@UseCaseScope
public interface InteractorsComponent {
    RegistrationUseCase registrationUseCase();
    LoginUseCase        loginUseCase();
    LogoutUseCase       logoutUseCase();
}
