package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.notifications.Notification;

import java.util.Objects;

public final class FriendshipNotification implements Notification {
    public static final long NOTIFICATION_TYPE = 3;

    private final Session scope;
    private final User    toNotify;

    public FriendshipNotification(final Session scope, final User toNotify) {
        this.scope    = Objects.requireNonNull(scope);
        this.toNotify = Objects.requireNonNull(toNotify);
    }

    @Override
    public long getType() {
        return NOTIFICATION_TYPE;
    }

    @Override
    public Session from() {
        return scope;
    }

    public User getUserToNotify() {
        return toNotify;
    }
}
