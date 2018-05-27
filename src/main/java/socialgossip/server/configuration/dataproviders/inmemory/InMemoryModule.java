package socialgossip.server.configuration.dataproviders.inmemory;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.configuration.dataproviders.UserRepositoryModule;
import socialgossip.server.core.gateways.session.SessionRepository;
import socialgossip.server.core.gateways.user.UserRepository;
import socialgossip.server.dataproviders.InMemoryRepository;

import java.util.Objects;

@Module(includes = UserRepositoryModule.class)
public class InMemoryModule {
    final InMemoryRepository repository;

    public InMemoryModule() {
        repository = new InMemoryRepository();
    }

    public InMemoryModule(final InMemoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Provides UserRepository provideUserRepository() {
        return repository;
    }

    @Provides SessionRepository provideSessionRepository() {
        return repository;
    }
}
