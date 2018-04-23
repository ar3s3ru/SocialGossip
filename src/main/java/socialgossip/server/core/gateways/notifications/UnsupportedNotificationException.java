package socialgossip.server.core.gateways.notifications;

public class UnsupportedNotificationException extends Exception {
    public UnsupportedNotificationException(final String message) {
        super("Unsupported Notification: " + message);
    }
}
