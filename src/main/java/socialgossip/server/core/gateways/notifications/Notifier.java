package socialgossip.server.core.gateways.notifications;

public interface Notifier {
    void register(NotificationHandler handler);
    void send(Notification notification) throws UnsupportedNotificationException;
}
