package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

import java.util.List;

public interface FriendshipsLister {
    List<Friendship> listFriendsOf(User user) throws GatewayException;
}
