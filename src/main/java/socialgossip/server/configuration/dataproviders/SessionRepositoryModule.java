package socialgossip.server.configuration.dataproviders;

import dagger.Binds;
import dagger.Module;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.session.SessionRemover;
import socialgossip.server.core.gateways.session.SessionRepository;

@Module
public abstract class SessionRepositoryModule {
    @Binds
    @DataproviderScope
    public abstract SessionFinder provideSessionFinder(SessionRepository repository);

    @Binds
    @DataproviderScope
    public abstract SessionInserter provideSessionInserter(SessionRepository repository);

    @Binds
    @DataproviderScope
    public abstract SessionRemover provideSessionRemover(SessionRepository repository);
}
