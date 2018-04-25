package socialgossip.server.usecases;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.GetSessionAccess;
import socialgossip.server.core.gateways.session.SessionNotFoundException;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class ProtectedUseCase<I extends PreAuthInput, O, E extends ProtectedErrorsHandler>
        extends AbstractUseCase<I, O, E>
        implements ProtectedResource {

    protected final GetSessionAccess sessionAccess;

    protected ProtectedUseCase(final GetSessionAccess sessionAccess) {
        this.sessionAccess = Objects.requireNonNull(sessionAccess);
    }

    protected abstract void onAuthorizedExecute(Session session, I input, Consumer<O> onSuccess, E errors);

    @Override
    protected final void onExecute(I input, Consumer<O> onSuccess, E errors) {
        try {
            final Session session = sessionAccess.getByToken(input.getSessionToken());
            this.checkAllowance(session);
            this.onAuthorizedExecute(session, input, onSuccess, errors);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        } catch (SessionNotFoundException e) {
            errors.onSessionNotFound(e);
        } catch (UnauthorizedException e) {
            errors.onUnauthorizedAccess(e);
        }
    }
}
