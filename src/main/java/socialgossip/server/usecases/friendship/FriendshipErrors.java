package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.friendship.InvalidFriendshipException;
import socialgossip.server.core.gateways.friendship.FriendshipAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;

public interface FriendshipErrors extends ProtectedErrorsHandler, GatewayErrorsHandler {
    void onUserNotFound(UserNotFoundException e);
    void onInvalidFriendship(InvalidFriendshipException e);
    void onFriendshipAlreadyExists(FriendshipAlreadyExistsException e);
}
