package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;

import java.util.Optional;

public interface GetFriendshipAccess {
    Optional<Friendship> getFriendshipDetails(Session requester, User with);
}
