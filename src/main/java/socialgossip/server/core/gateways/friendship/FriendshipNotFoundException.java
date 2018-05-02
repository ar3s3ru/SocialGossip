package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.user.User;

public class FriendshipNotFoundException extends Exception {
    public FriendshipNotFoundException(final User from, final User to) {
        super("no friendship found between \"" +
                from.getId() + "\" and \"" +
                to.getId()   + "\"");
    }
}
