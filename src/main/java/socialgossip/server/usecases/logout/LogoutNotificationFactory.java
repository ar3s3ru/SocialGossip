package socialgossip.server.usecases.logout;

import socialgossip.server.core.entities.session.Session;

@FunctionalInterface
public interface LogoutNotificationFactory {
    LogoutNotification produce(Session session);
}
