package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.UserNotFoundException;

public interface GetUserWithFriendshipAccess {
    UserFriendship getUserWithFriendship(Session requester, String username)
            throws UserNotFoundException, GatewayException;
}
