package socialgossip.server.core.gateways.notifications;

import socialgossip.server.core.entities.session.SessionScoped;

public interface Notifier {
    void register(NotificationHandler handler);
    void unregister(SessionScoped scope);
    void send(Notification notification) throws UnsupportedNotificationException;
}
