package socialgossip.server.configuration.interactors;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionRemover;
import socialgossip.server.usecases.logout.LogoutInteractor;
import socialgossip.server.usecases.logout.LogoutUseCase;

@Module
public class LogoutModule {
    @Provides
    @UseCaseScope static LogoutUseCase provideLogoutInteractor(
            final SessionFinder  sessionFinder,
            final SessionRemover sessionRemover,
            final Notifier notifier
    ) {
        return new LogoutInteractor(sessionFinder, sessionRemover, notifier);
    }
}
