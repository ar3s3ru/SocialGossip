package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;

public class FriendshipAlreadyExistsException extends Exception {
    public FriendshipAlreadyExistsException(final Friendship friendship) {
        super("friendship between " +
                friendship.getSubjects()[0].getId() +
                " and " +
                friendship.getSubjects()[1].getId() + " already exists");
    }
}
