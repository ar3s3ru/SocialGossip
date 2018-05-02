package socialgossip.server.usecases.lookup;

import socialgossip.server.core.entities.auth.Permission;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.friendship.GetUserWithFriendshipAccess;
import socialgossip.server.core.gateways.friendship.UserFriendship;
import socialgossip.server.core.gateways.session.GetSessionAccess;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class LookupInteractor
        extends ProtectedUseCase<LookupUseCase.Input, LookupDetails, LookupErrors>
        implements LookupUseCase {

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
            onSuccess.accept(createNewLookupDetailsFrom(details));
        } catch (UserNotFoundException e) {
            errors.onUserNotFound(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }

    private UserFriendship retrieveUserFriendshipDetails(final LookupUseCase.Input input,
                                                         final Session session)
            throws UserNotFoundException, GatewayException {
        final UserFriendship friendship = friendshipAccess.getUserWithFriendship(session, input.getLookupUsername());
        return friendship;
    }

    private LookupDetails createNewLookupDetailsFrom(final UserFriendship friendshipDetails) {
        return new LookupDetails(friendshipDetails.getUser().getId(),friendshipDetails.getFriendshipDate());
    }
}
