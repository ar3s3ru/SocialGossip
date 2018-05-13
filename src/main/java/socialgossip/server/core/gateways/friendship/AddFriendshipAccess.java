package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.gateways.GatewayException;

public interface AddFriendshipAccess {
    void add(Friendship friendship) throws FriendshipAlreadyExistsException, GatewayException;
}
