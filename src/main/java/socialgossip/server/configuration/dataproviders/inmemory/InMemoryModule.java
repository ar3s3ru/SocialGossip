package socialgossip.server.configuration.dataproviders;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.dataproviders.InMemoryRepository;

import javax.inject.Singleton;
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

    @Provides
    @Singleton
    public UserRepository provideUserRepository() {
        return repository;
    }
}
