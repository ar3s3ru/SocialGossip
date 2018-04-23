package socialgossip.server.core.gateways.notifications;

import socialgossip.server.core.entities.session.SessionScoped;

public interface NotificationHandler extends SessionScoped {
    long getType();
    void handle(Notification notification) throws UnsupportedNotificationException;
}
