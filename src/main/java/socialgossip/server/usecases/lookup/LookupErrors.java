package socialgossip.server.usecases.lookup;

import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;

public interface LookupErrors
        extends ProtectedErrorsHandler, GatewayErrorsHandler {
    void onUserNotFound(UserNotFoundException e);
}
