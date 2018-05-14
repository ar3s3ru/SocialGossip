package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.friendship.InvalidFriendshipException;
import socialgossip.server.core.entities.user.User;

@FunctionalInterface
public interface FriendshipFactory {
    Friendship produce(User requester, User friend) throws InvalidFriendshipException;
}
