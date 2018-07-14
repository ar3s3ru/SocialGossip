package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.friendship.InvalidFriendshipException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.friendship.FriendshipAlreadyExistsException;
import socialgossip.server.core.gateways.friendship.FriendshipInserter;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class FriendshipInteractor
        extends ProtectedUseCase<FriendshipUseCase.Input, FriendshipOutput>
        implements FriendshipUseCase{

    private static final Logger LOG = Logger.getLogger(FriendshipInteractor.class.getName());

    private final UserFinder userFinder;
    private final FriendshipFactory  friendshipFactory;
    private final FriendshipInserter friendshipInserter;

    private final Notifier                      notifier;
    private final FriendshipNotificationFactory notificationFactory;

    public FriendshipInteractor(final SessionFinder                 sessionFinder,
                                final UserFinder                    userFinder,
                                final FriendshipFactory             friendshipFactory,
                                final FriendshipInserter            friendshipInserter,
                                final Notifier                      notifier,
                                final FriendshipNotificationFactory notificationFactory) {
        super(sessionFinder);
        this.userFinder          = Objects.requireNonNull(userFinder);
        this.friendshipFactory   = Objects.requireNonNull(friendshipFactory);
        this.friendshipInserter  = Objects.requireNonNull(friendshipInserter);
        this.notifier            = Objects.requireNonNull(notifier);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    protected void onAuthorizedExecute(final Session session,
                                       final FriendshipUseCase.Input input,
                                       final Consumer<FriendshipOutput> onSuccess,
                                       final Consumer<Throwable>        onError) {
        try {
            final User target = getTargetUserFrom(input);
            final Friendship friendship = produceNewFriendship(input, session.getUser(), target);
            trySavingFriendship(input, friendship);
            sendFriendshipNotification(input, session, target);
            onSuccess.accept(produceFriendshipOutput(input, friendship));
        } catch (UserNotFoundException           | InvalidFriendshipException |
                FriendshipAlreadyExistsException | GatewayException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            onError.accept(e);
        }
    }

    private User getTargetUserFrom(final FriendshipUseCase.Input input)
            throws UserNotFoundException, GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "retrieving target friend: " + input.getFriendUsername());
        final User target = userFinder.findByUsername(input.getFriendUsername());
        AppLogger.fine(LOG, input::getRequestId, () -> "found target User: " + target);
        return target;
    }

    private Friendship produceNewFriendship(final FriendshipUseCase.Input input,
                                            final User requester, final User target)
            throws InvalidFriendshipException {
        AppLogger.fine(LOG, input::getRequestId,
                () -> "creating new friendship between" + requester.getId() + " and " + target.getId());
        final Friendship friendship = friendshipFactory.produce(requester, target);
        AppLogger.fine(LOG, input::getRequestId, () -> "Friendship created: " + friendship);
        return friendship;
    }

    private void trySavingFriendship(final FriendshipUseCase.Input input,
                                     final Friendship friendship)
            throws FriendshipAlreadyExistsException, GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "saving Friendship: " + friendship);
        friendshipInserter.insert(friendship);
        AppLogger.info(LOG, input::getRequestId, () -> "saved Friendship successfully: " + friendship);
    }

    private FriendshipOutput produceFriendshipOutput(final FriendshipUseCase.Input input,
                                                     final Friendship friendship) {
        return new FriendshipOutput(input.getFriendUsername(), friendship.getIssueDate());
    }

    private void sendFriendshipNotification(final FriendshipUseCase.Input input,
                                            final Session requester, final User target) {
        try {
            final FriendshipNotification notification =
                    notificationFactory.produce(requester, target);
            AppLogger.fine(LOG, input::getRequestId, () -> "sending Friendship notification: " + notification);
            notifier.send(notification);
            AppLogger.info(LOG, input::getRequestId, () -> "Friendship notification sent: " + notification);
        } catch (UnsupportedNotificationException e) {
            AppLogger.warn(LOG, input::getRequestId, () -> "Friendship notification failed: " + e);
        }
    }
}
