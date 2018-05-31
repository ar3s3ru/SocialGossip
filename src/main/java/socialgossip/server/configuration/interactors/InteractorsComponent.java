package socialgossip.server.configuration.interactors;

import dagger.Subcomponent;
import socialgossip.server.usecases.login.LoginErrors;
import socialgossip.server.usecases.login.LoginOutput;
import socialgossip.server.usecases.login.LoginUseCase;
import socialgossip.server.usecases.logout.LogoutErrors;
import socialgossip.server.usecases.logout.LogoutUseCase;
import socialgossip.server.usecases.registration.RegistrationErrors;
import socialgossip.server.usecases.registration.RegistrationUseCase;

@Subcomponent(modules = {
        RegistrationModule.class,
        LoginModule.class,
        LogoutModule.class
})
@UseCaseScope
public interface InteractorsComponent {
    RegistrationUseCase<Void, RegistrationErrors> registrationUseCase();
    LoginUseCase<LoginOutput, LoginErrors>        loginUseCase();
    LogoutUseCase<Boolean, LogoutErrors>          logoutUseCase();
}
