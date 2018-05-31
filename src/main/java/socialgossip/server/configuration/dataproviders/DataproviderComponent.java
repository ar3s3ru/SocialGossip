package socialgossip.server.configuration.dataproviders;

import dagger.Subcomponent;
import socialgossip.server.configuration.dataproviders.inmemory.InMemoryModule;
import socialgossip.server.configuration.interactors.InteractorsComponent;
import socialgossip.server.configuration.interactors.LoginModule;
import socialgossip.server.configuration.interactors.LogoutModule;
import socialgossip.server.configuration.interactors.RegistrationModule;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.session.SessionRemover;
import socialgossip.server.core.gateways.session.SessionRepository;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.core.gateways.user.UserRepository;

@Subcomponent(modules = InMemoryModule.class)
@DataproviderScope
public interface DataproviderComponent {
    UserRepository    userRepository();
    UserFinder        userFinder();
    UserInserter      userInserter();

    SessionRepository sessionRepository();
    SessionFinder     sessionFinder();
    SessionInserter   sessionInserter();
    SessionRemover    sessionRemover();

    InteractorsComponent attachInteractorsComponent(
            RegistrationModule registrationModule,
            LoginModule        loginModule,
            LogoutModule       logoutModule
    );
}
