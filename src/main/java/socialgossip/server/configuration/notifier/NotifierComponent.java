package socialgossip.server.configuration.notifier;

import dagger.Subcomponent;
import socialgossip.server.configuration.interactors.InteractorsComponent;
import socialgossip.server.configuration.interactors.LoginModule;
import socialgossip.server.configuration.interactors.LogoutModule;
import socialgossip.server.configuration.interactors.RegistrationModule;
import socialgossip.server.core.gateways.notifications.Notifier;

@Subcomponent(modules = NotifierModule.class)
@NotifierScope
public interface NotifierComponent {
    Notifier notifier();

    InteractorsComponent attachInteractorsComponent(
            RegistrationModule registrationModule,
            LoginModule        loginModule,
            LogoutModule       logoutModule
    );
}
