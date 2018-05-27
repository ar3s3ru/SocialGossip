package socialgossip.server.configuration.dataproviders;

import dagger.Component;
import socialgossip.server.configuration.dataproviders.inmemory.InMemoryModule;
import socialgossip.server.core.gateways.session.SessionRepository;
import socialgossip.server.core.gateways.user.UserRepository;

@Component(modules = InMemoryModule.class)
public interface DataproviderComponent {
    UserRepository    userRepository();
    SessionRepository sessionRepository();
}
