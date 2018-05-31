package socialgossip.server.configuration.dataproviders;

import dagger.Binds;
import dagger.Module;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.core.gateways.user.UserRepository;

@Module
public abstract class UserRepositoryModule {
    @Binds
    @DataproviderScope
    public abstract UserFinder provideUserFinder(UserRepository repository);

    @Binds
    @DataproviderScope
    public abstract UserInserter provideUserInserter(UserRepository repository);
}
