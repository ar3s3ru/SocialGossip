package socialgossip.server.notifier;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.session.SessionScoped;
import socialgossip.server.core.gateways.notifications.Notification;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public final class SimpleNotifier implements Notifier {

    private final ConcurrentMap<Session, ConcurrentMap<Long, NotificationHandler>> registeredHandlersMap;
    private final ThreadPoolExecutor executor;

    public SimpleNotifier(final ThreadPoolExecutor executor) {
        this.registeredHandlersMap = new ConcurrentHashMap<>();
        this.executor = Objects.requireNonNull(executor);
    }

    @Override
    public void register(final NotificationHandler handler) {
        Objects.requireNonNull(handler);
        registeredHandlersMap.compute(
                Objects.requireNonNull(handler.from()),
                (session, handlers) -> {
                    final ConcurrentMap<Long, NotificationHandler> map =
                            Optional.ofNullable(handlers).orElseGet(ConcurrentHashMap::new);
                    map.put(handler.getType(), handler);
                    return map;
                });
    }

    @Override
    public void unregister(final SessionScoped scope) {
        Objects.requireNonNull(scope);
        registeredHandlersMap.remove(scope.from());
    }

    @Override
    public void send(final Notification notification) throws UnsupportedNotificationException {
        // Ignore future result for this implementation: it'll work like a scheduled job of sorts
        sendWithResult(notification);
    }

    Future<Boolean> sendWithResult(final Notification notification) throws UnsupportedNotificationException {
        Objects.requireNonNull(notification);
        final ConcurrentMap<Long, NotificationHandler> handlersMap =
                Optional.ofNullable(registeredHandlersMap.get(notification.from()))
                        .orElseThrow(() -> new UnsupportedNotificationException("no registered handlers for this session"));
        final NotificationHandler handler =
                Optional.ofNullable(handlersMap.get(notification.getType()))
                        .orElseThrow(() -> new UnsupportedNotificationException("no handler for type: " + notification.getType()));
        return executor.submit(() -> {
            try {
                handler.handle(notification);
                return true;
            } catch (UnsupportedNotificationException e) {
                // Log here!
                return false;
            }
        });
    }
}
