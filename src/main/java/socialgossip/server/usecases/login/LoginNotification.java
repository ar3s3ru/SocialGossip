package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.notifications.Notification;

import java.util.Objects;

public final class LoginNotification implements Notification {
    public static final long NOTIFICATION_TYPE = 1;

    private final Session scope;

    public LoginNotification(final Session scope) {
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
