package socialgossip.server.usecases.logout;

import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;

public interface LogoutErrors extends ProtectedErrorsHandler, GatewayErrorsHandler {
    void onSessionNotFound(SessionNotFoundException e);
}
