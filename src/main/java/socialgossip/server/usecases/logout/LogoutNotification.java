package socialgossip.server.usecases.logout;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.notifications.Notification;

import java.util.Objects;

public final class LogoutNotification implements Notification {
    public static final long NOTIFICATION_TYPE = 2;

    private final Session scope;

    public LogoutNotification(final Session scope) {
        this.scope = Objects.requireNonNull(scope);
    }

    @Override
    public long getType() {
        return NOTIFICATION_TYPE;
    }

    @Override
    public Session from() {
        return scope;
    }
}
