package socialgossip.server.usecases.friends;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.friendship.FriendshipsLister;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.ProtectedUseCase;
import socialgossip.server.usecases.friendship.FriendshipOutput;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class FriendsInteractor
        extends ProtectedUseCase<FriendsUseCase.Input, List<FriendshipOutput>>
        implements FriendsUseCase {

    private static final Logger LOG = Logger.getLogger(FriendsInteractor.class.getName());

    private final FriendshipsLister friendshipsLister;

    public FriendsInteractor(final SessionFinder sessionAccess,
                             final FriendshipsLister friendshipsLister) {
        super(sessionAccess);
        this.friendshipsLister = Objects.requireNonNull(friendshipsLister);
    }

    @Override
    protected void onAuthorizedExecute(final Session session,
                                       final FriendsUseCase.Input input,
                                       final Consumer<List<FriendshipOutput>> onSuccess,
                                       final Consumer<Throwable> onError) {
        try {
            final List<Friendship> friendships = retrieveFriendshipsOf(input, session.getUser());
            onSuccess.accept(produceFriendshipsOutput(input, session.getUser(), friendships));
        } catch (GatewayException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            onError.accept(e);
        }
    }

    private List<Friendship> retrieveFriendshipsOf(final FriendsUseCase.Input input,
                                                   final User user)
            throws GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "retrieving friends for User " + user);
        final List<Friendship> friendships = friendshipsLister.listFriendsOf(user);
        AppLogger.fine(LOG, input::getRequestId, () -> "friendships retrieved: " + friendships);
        return friendships;
    }

    private List<FriendshipOutput> produceFriendshipsOutput(final FriendsUseCase.Input input,
                                                            final User requester,
                                                            final List<Friendship> friendships) {
        final List<Friendship> from = Optional.ofNullable(friendships).orElseGet(Collections::emptyList);
        AppLogger.fine(LOG, input::getRequestId, () -> "producing output from friendships: " + from);
        final List<FriendshipOutput> output = friendships.stream().map(friendship -> {
            final User[] users = friendship.getSubjects();
            final User friend = Arrays.stream(users).filter(u -> !u.equals(requester)).findFirst().get();
            AppLogger.fine(LOG, input::getRequestId, () -> "adding User to friends list: " + friend);
            return new FriendshipOutput(friend.getId(), friendship.getIssueDate());
        }).collect(Collectors.toList());
        AppLogger.fine(LOG, input::getRequestId, () -> "output produced: " + output);
        return output;
    }
}
