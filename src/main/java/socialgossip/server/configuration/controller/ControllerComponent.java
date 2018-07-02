package socialgossip.server.configuration.controller;

import dagger.Component;
import socialgossip.server.configuration.interactors.InteractorsComponent;
import socialgossip.server.configuration.presentation.PresentationComponent;
import socialgossip.server.entrypoints.tcp.authorized.logout.LogoutController;
import socialgossip.server.entrypoints.tcp.login.LoginController;
import socialgossip.server.entrypoints.tcp.registration.RegistrationController;

@Component(
        dependencies = {
                PresentationComponent.class,
                InteractorsComponent.class
        },
        modules = {
                RegistrationControllerModule.class,
                LoginControllerModule.class,
                LogoutControllerModule.class
        }
)
@ControllerScope
public interface ControllerComponent {
    RegistrationController registrationController();
    LoginController        loginController();
    LogoutController       logoutController();
}
