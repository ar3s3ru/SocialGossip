package socialgossip.server.usecases.friends;

import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;

public interface FriendsErrors extends ProtectedErrorsHandler, GatewayErrorsHandler {
}
