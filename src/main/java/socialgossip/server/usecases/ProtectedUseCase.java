package socialgossip.server.usecases;

import socialgossip.server.core.entities.auth.Permission;
import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.SessionFinder;
import socialgossip.server.core.gateways.session.SessionNotFoundException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class ProtectedUseCase<I extends PreAuthInput, O, E extends ProtectedErrorsHandler>
        extends AbstractUseCase<I, O, E>
        implements ProtectedResource {

    protected final SessionFinder sessionFinder;

    protected ProtectedUseCase(final SessionFinder sessionAccess) {
        this.sessionFinder = Objects.requireNonNull(sessionAccess);
    }

    protected abstract void onAuthorizedExecute(Session session, I input, Consumer<O> onSuccess, E errors);

    @Override
    public void checkAllowance(final Permission permission) throws UnauthorizedException {
        Optional.ofNullable(permission).orElseThrow(
                () -> new UnauthorizedException("<null>", "null tokens can't be authorized on logout")
        );
        if (permission.isExpired()) {
            throw new UnauthorizedException(permission.getToken(), "permission expired");
        }
    }

    @Override
    protected final void onExecute(I input, Consumer<O> onSuccess, E errors) {
        try {
            final Session session = sessionFinder.findByToken(input.getSessionToken());
            this.checkAllowance(session);
            this.onAuthorizedExecute(session, input, onSuccess, errors);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        } catch (SessionNotFoundException e) {
            errors.onUnauthorizedAccess(
                    new UnauthorizedException(input.getSessionToken(), e.getMessage())
            );
        } catch (UnauthorizedException e) {
            errors.onUnauthorizedAccess(e);
        }
    }
}
