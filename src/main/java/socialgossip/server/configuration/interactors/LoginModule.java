package socialgossip.server.configuration.interactors;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.session.SessionScoped;
import socialgossip.server.core.gateways.notifications.Notification;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.login.LoginInteractor;
import socialgossip.server.usecases.login.SessionFactory;

import java.util.logging.Logger;

@Module(includes = InteractorsModule.class)
public class LoginModule {
    @Provides
    @UseCaseScope static LoginInteractor providesLoginInteractor(
            final UserFinder        userFinder,
            final SessionInserter   sessionInserter,
            final SessionFactory    sessionFactory,
            final PasswordValidator passwordValidator
    ) {
        // TODO: remove notifier
        return new LoginInteractor(userFinder, sessionInserter, passwordValidator, sessionFactory,
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
