package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.gateways.GatewayException;

public interface FriendshipInserter {
    void insert(Friendship friendship)
            throws FriendshipAlreadyExistsException, GatewayException;
}
