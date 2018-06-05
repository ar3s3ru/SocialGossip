package socialgossip.server.usecases;

import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;

public interface ProtectedErrorsHandler extends GatewayErrorsHandler {
    void onUnauthorizedAccess(UnauthorizedException e);
}