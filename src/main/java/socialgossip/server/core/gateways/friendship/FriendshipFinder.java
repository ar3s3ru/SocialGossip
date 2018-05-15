package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

public interface FriendshipFinder {
    Friendship findFriendshipBetween(Session requester, User with)
            throws FriendshipNotFoundException, GatewayException;
}
