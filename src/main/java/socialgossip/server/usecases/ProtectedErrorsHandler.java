package socialgossip.server.usecases;

import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.SessionNotFoundException;

public interface ProtectedErrorsHandler extends ErrorsHandler {
    void onUnauthorizedAccess(UnauthorizedException e);
    void onSessionNotFound(SessionNotFoundException e);
    void onGatewayError(GatewayException e);
}
