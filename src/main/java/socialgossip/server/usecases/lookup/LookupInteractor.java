package socialgossip.server.usecases.lookup;

import socialgossip.server.core.entities.auth.Permission;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.friendship.GetUserWithFriendshipAccess;
import socialgossip.server.core.gateways.friendship.UserFriendship;
import socialgossip.server.core.gateways.session.GetSessionAccess;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.ProtectedUseCase;
import socialgossip.server.usecases.logging.UseCaseLogger;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class LookupInteractor
        extends ProtectedUseCase<LookupUseCase.Input, LookupDetails, LookupErrors>
        implements LookupUseCase {

    private static final Logger LOG = Logger.getLogger(LookupInteractor.class.getName());

    private final GetUserWithFriendshipAccess friendshipAccess;

    public LookupInteractor(final GetUserWithFriendshipAccess friendshipAccess,
                            final GetSessionAccess            sessionAccess) {
        super(sessionAccess);
        this.friendshipAccess = Objects.requireNonNull(friendshipAccess);
    }

    @Override
    public void checkAllowance(final Permission permission) throws UnauthorizedException {
        Optional.ofNullable(permission).orElseThrow(
                () -> new UnauthorizedException("<null>", "null tokens can't be authorized on logout")
        );
    }

    @Override
    protected void onAuthorizedExecute(final Session                 session,
                                       final LookupUseCase.Input     input,
                                       final Consumer<LookupDetails> onSuccess,
                                       final LookupErrors            errors) {
        try {
           final UserFriendship details = retrieveUserFriendshipDetails(input, session);
           onSuccess.accept(createLookupDetailsResultFrom(details));
        } catch (UserNotFoundException e) {
            errors.onUserNotFound(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }

    private UserFriendship retrieveUserFriendshipDetails(final LookupUseCase.Input input,
                                                         final Session session)
            throws UserNotFoundException, GatewayException {
        UseCaseLogger.fine(LOG, input, () -> "retrieving friendship info with " + input.getLookupUsername());
        final UserFriendship friendship = friendshipAccess.getUserWithFriendship(session, input.getLookupUsername());
        UseCaseLogger.fine(LOG, input, () -> "query returned: " + friendship.toString());
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
