package socialgossip.server.usecases.logout;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.core.gateways.session.SessionRemover;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class LogoutInteractor
    extends ProtectedUseCase<LogoutUseCase.Input, Boolean>
    implements LogoutUseCase {

    private static final Logger LOG = Logger.getLogger(LogoutInteractor.class.getName());

    private final SessionRemover            sessionRemover;
    private final Notifier                  notifier;
    private final LogoutNotificationFactory notificationFactory;

    public LogoutInteractor(final SessionFinder getSessionAccess,
                            final SessionRemover removeSessionAccess,
                            final Notifier            notifier) {
        this(getSessionAccess, removeSessionAccess, notifier, LogoutNotification::new);
    }

    public LogoutInteractor(final SessionFinder             sessionFinder,
                            final SessionRemover            sessionRemover,
                            final Notifier                  notifier,
                            final LogoutNotificationFactory notificationFactory) {
        super(sessionFinder);
        this.sessionRemover      = Objects.requireNonNull(sessionRemover);
        this.notifier            = Objects.requireNonNull(notifier);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    protected void onAuthorizedExecute(final Session             session,
                                       final LogoutUseCase.Input input,
                                       final Consumer<Boolean>   onSuccess,
                                       final Consumer<Throwable> onError) {
        try {
            tryRemovingSession(input, session);
            trySendingLogoutNotification(input, session);
            onSuccess.accept(true);
        } catch (SessionNotFoundException | GatewayException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            onError.accept(e);
        }
    }

    private void tryRemovingSession(final LogoutUseCase.Input input, final Session session)
            throws SessionNotFoundException, GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "removing Session \"" + session.getToken() + "\"...");
        sessionRemover.remove(session);
        AppLogger.info(LOG, input::getRequestId, () -> "Session \"" + session.getToken() + "\" removed successfully!");
    }

    private void trySendingLogoutNotification(final LogoutUseCase.Input input, final Session session) {
        final LogoutNotification notification = notificationFactory.produce(session);
        try {
            AppLogger.fine(LOG, input::getRequestId, () -> "sending logout notification...");
            notifier.send(notification);
            AppLogger.fine(LOG, input::getRequestId, () -> "logout notification successfully delivered!");
        } catch (UnsupportedNotificationException e) {
            AppLogger.warn(LOG, input::getRequestId, () -> "UnsupportedNotificationException: " + e);
        }
        notifier.unregister(notification); // LogoutNotification is Session-scoped!
        AppLogger.fine(LOG, input::getRequestId,
                () -> "unregistered notification handlers for Session \"" + session.getToken() + "\""
        );
    }
}
