package socialgossip.server.configuration.dataproviders.user;

import dagger.Binds;
import dagger.Module;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.core.gateways.user.UserRepository;

import javax.inject.Singleton;

@Module
public abstract class UserRepositoryModule {
    @Binds
    @Singleton
    public abstract UserFinder provideUserFinder(UserRepository repository);

    @Binds
    @Singleton
    public abstract UserInserter provideUserInserter(UserRepository repository);
}
