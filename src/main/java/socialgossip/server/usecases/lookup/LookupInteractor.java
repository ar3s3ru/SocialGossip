package socialgossip.server.usecases.lookup;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.friendship.UserFriendship;
import socialgossip.server.core.gateways.friendship.UserFriendshipFinder;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class LookupInteractor
        extends ProtectedUseCase<LookupUseCase.Input, LookupDetails>
        implements LookupUseCase {

    private static final Logger LOG = Logger.getLogger(LookupInteractor.class.getName());

    private final UserFriendshipFinder friendshipFinder;

    public LookupInteractor(final SessionFinder        sessionFinder,
                            final UserFriendshipFinder friendshipFinder) {
        super(sessionFinder);
        this.friendshipFinder = Objects.requireNonNull(friendshipFinder);
    }

    @Override
    protected void onAuthorizedExecute(final Session                 session,
                                       final LookupUseCase.Input     input,
                                       final Consumer<LookupDetails> onSuccess,
                                       final Consumer<Throwable>     onError) {
        try {
           final UserFriendship details = retrieveUserFriendshipDetails(input, session);
           onSuccess.accept(createLookupDetailsResultFrom(details));
        } catch (UserNotFoundException | GatewayException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            onError.accept(e);
        }
    }

    private UserFriendship retrieveUserFriendshipDetails(final LookupUseCase.Input input,
                                                         final Session session)
            throws UserNotFoundException, GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "retrieving friendship info with " + input.getLookupUsername());
        final UserFriendship friendship = friendshipFinder.findUserFriendshipBetween(session, input.getLookupUsername());
        AppLogger.fine(LOG, input::getRequestId, () -> "query returned: " + friendship.toString());
        return friendship;
    }

    private LookupDetails createLookupDetailsResultFrom(final UserFriendship details) {
        return details.onFriendship()
                .map(f -> createOnFriendshipPresent(details, f))
                .orElse(createOnFriendshipMissing(details));
    }

    private LookupDetails createOnFriendshipMissing(final UserFriendship details) {
        return new LookupDetails(details.getUser().getId());
    }

    private LookupDetails createOnFriendshipPresent(final UserFriendship details, final Friendship friendship) {
        return new LookupDetails(
                details.getUser().getId(),
                friendship.getIssueDate()
        );
    }
}
