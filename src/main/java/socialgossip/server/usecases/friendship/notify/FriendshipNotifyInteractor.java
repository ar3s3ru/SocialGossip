package socialgossip.server.usecases.friendship.notify;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class FriendshipNotifyInteractor
        extends ProtectedUseCase<FriendshipNotifyUseCase.Input, Void, ProtectedErrorsHandler>
        implements FriendshipNotifyUseCase<Void, ProtectedErrorsHandler> {

    private static final Logger LOG = Logger.getLogger(FriendshipNotifyInteractor.class.getName());
    private final Notifier notifier;

    public FriendshipNotifyInteractor(final SessionFinder sessionAccess, final Notifier notifier) {
        super(sessionAccess);
        this.notifier = Objects.requireNonNull(notifier);
    }

    @Override
    protected void onAuthorizedExecute(final Session session,
                                       final FriendshipNotifyUseCase.Input input,
                                       final Consumer<Void> onSuccess,
                                       final ProtectedErrorsHandler errors) {
        Objects.requireNonNull(input.handlerFactory());
        final NotificationHandler handler = input.handlerFactory().apply(session);
        Objects.requireNonNull(handler);
        notifier.register(handler);
        AppLogger.info(LOG, input::getRequestId, () -> "Friendship notification handler registered!");
        onSuccess.accept(null);
    }
}
