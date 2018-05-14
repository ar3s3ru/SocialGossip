package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;

@FunctionalInterface
public interface FriendshipNotificationFactory {
    FriendshipNotification produce(Session scope, User toNotify);
}
