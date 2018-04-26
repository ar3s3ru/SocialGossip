package socialgossip.server.usecases;

import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.gateways.GatewayException;

public interface ProtectedErrorsHandler extends ErrorsHandler {
    void onUnauthorizedAccess(UnauthorizedException e);
    void onGatewayError(GatewayException e);
}
