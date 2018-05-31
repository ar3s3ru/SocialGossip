package socialgossip.server.configuration.interactors;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.entities.session.SessionScoped;
import socialgossip.server.core.gateways.notifications.Notification;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionRemover;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.logout.LogoutInteractor;

import java.util.logging.Logger;

@Module(includes = InteractorsModule.class)
public class LogoutModule {
    @Provides
    @UseCaseScope static LogoutInteractor provideLogoutInteractor(
            final SessionFinder  sessionFinder,
            final SessionRemover sessionRemover
    ) {
        // TODO: remove notifier
        return new LogoutInteractor(sessionFinder, sessionRemover,
                new Notifier() {
                    @Override
                    public void register(final NotificationHandler handler) {
                        AppLogger.fine(Logger.getGlobal(), null, () -> "register: " + handler);
                    }

                    @Override
                    public void unregister(final SessionScoped scope) {
                        AppLogger.fine(Logger.getGlobal(), null, () -> "unregister: " + scope);
                    }

                    @Override
                    public void send(final Notification notification) {
                        AppLogger.fine(Logger.getGlobal(), null, () -> "send: " + notification);
                    }
                });
    }
}
