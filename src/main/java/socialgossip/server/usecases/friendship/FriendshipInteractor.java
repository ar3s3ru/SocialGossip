package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.friendship.InvalidFriendshipException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.friendship.AddFriendshipAccess;
import socialgossip.server.core.gateways.friendship.FriendshipAlreadyExistsException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.GetSessionAccess;
import socialgossip.server.core.gateways.user.GetUserAccess;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.function.Consumer;

public final class FriendshipInteractor
        extends ProtectedUseCase<FriendshipUseCase.Input, FriendshipOutput, FriendshipErrors>
        implements FriendshipUseCase {

    private final GetUserAccess       userAccess;
    private final FriendshipFactory   friendshipFactory;
    private final AddFriendshipAccess friendshipAccess;

    private final Notifier                      notifier;
    private final FriendshipNotificationFactory notificationFactory;

    public FriendshipInteractor(final GetSessionAccess sessionAccess,
                                final GetUserAccess userAccess,
                                final FriendshipFactory friendshipFactory,
                                final AddFriendshipAccess friendshipAccess,
                                final Notifier notifier,
                                final FriendshipNotificationFactory notificationFactory) {
        super(sessionAccess);
        this.userAccess          = Objects.requireNonNull(userAccess);
        this.friendshipFactory   = Objects.requireNonNull(friendshipFactory);
        this.friendshipAccess    = Objects.requireNonNull(friendshipAccess);
        this.notifier            = Objects.requireNonNull(notifier);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    protected void onAuthorizedExecute(final Session session,
                                       final FriendshipUseCase.Input input,
                                       final Consumer<FriendshipOutput> onSuccess,
                                       final FriendshipErrors errors) {
        try {
            final User target = getTargetUserFrom(input);
            final Friendship friendship = produceNewFriendship(input, session.getUser(), target);
            trySavingFriendship(input, friendship);
            sendFriendshipNotification(input, session, target);
            onSuccess.accept(produceFriendshipOutput(input, friendship));
        } catch (UserNotFoundException e) {
            errors.onUserNotFound(e);
        } catch (InvalidFriendshipException e) {
            errors.onInvalidFriendship(e);
        } catch (FriendshipAlreadyExistsException e) {
            errors.onFriendshipAlreadyExists(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }

    private User getTargetUserFrom(final FriendshipUseCase.Input input)
            throws UserNotFoundException, GatewayException {
        final User target = userAccess.getByUsername(input.getFriendUsername());
        return target;
    }

    private Friendship produceNewFriendship(final FriendshipUseCase.Input input,
                                            final User requester, final User target)
            throws InvalidFriendshipException {
        final Friendship friendship = friendshipFactory.produce(requester, target);
        return friendship;
    }

    private void trySavingFriendship(final FriendshipUseCase.Input input,
                                     final Friendship friendship)
            throws FriendshipAlreadyExistsException, GatewayException {
        friendshipAccess.add(friendship);
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
            notifier.send(notification);
        } catch (UnsupportedNotificationException e) {
            // TODO(ar3s3ru): add log-print
        }
    }
}
