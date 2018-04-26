package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.session.Session;

@FunctionalInterface
public interface LoginNotificationFactory {
    LoginNotification produce(Session scope);
}
