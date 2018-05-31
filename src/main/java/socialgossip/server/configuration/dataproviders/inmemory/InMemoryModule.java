package socialgossip.server.configuration.dataproviders.inmemory;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.configuration.dataproviders.DataproviderScope;
import socialgossip.server.configuration.dataproviders.SessionRepositoryModule;
import socialgossip.server.configuration.dataproviders.UserRepositoryModule;
import socialgossip.server.core.gateways.session.SessionRepository;
import socialgossip.server.core.gateways.user.UserRepository;
import socialgossip.server.dataproviders.InMemoryRepository;

import java.util.Objects;

@Module(includes = {UserRepositoryModule.class, SessionRepositoryModule.class})
public class InMemoryModule {
    final InMemoryRepository repository;

    public InMemoryModule() {
        repository = new InMemoryRepository();
    }

    public InMemoryModule(final InMemoryRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Provides
    @DataproviderScope
    UserRepository provideUserRepository() {
        return repository;
    }

    @Provides
    @DataproviderScope SessionRepository provideSessionRepository() {
        return repository;
    }
}
