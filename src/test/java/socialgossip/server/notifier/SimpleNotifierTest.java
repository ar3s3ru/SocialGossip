package socialgossip.server.notifier;

import org.junit.Test;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.notifications.Notification;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.usecases.login.LoginNotification;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleNotifierTest {

    private static final ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    @Test
    public void sendNotificationWithNoHandlers_shouldRaiseException() {
        final SimpleNotifier notifier = new SimpleNotifier(executor);
        final Session session = mock(Session.class);
        when(session.getToken()).thenReturn("testToken");

        try {
            notifier.send(new LoginNotification(session));
            fail("Expected expection to be raised");
        } catch (UnsupportedNotificationException ok) {}
    }

    @Test
    public void registerHandlerAndSendNotification_shouldNotRaiseExceptions() {
        final SimpleNotifier notifier = new SimpleNotifier(executor);
        final Session session = mock(Session.class);
        when(session.getToken()).thenReturn("testToken2");

        final Notification notification = new LoginNotification(session);

        final NotificationHandler handler = mock(NotificationHandler.class);
        when(handler.getType()).thenReturn(LoginNotification.NOTIFICATION_TYPE);
        when(handler.from()).thenReturn(session);

        try {
            notifier.register(handler);
            final Future<Boolean> result = notifier.sendWithResult(notification);
            assertTrue(result.get());
        } catch (UnsupportedNotificationException | InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void registerHandlerAndSendWrongNotification_shouldRaiseExceptions() {
        final SimpleNotifier notifier = new SimpleNotifier(executor);
        final Session session = mock(Session.class);
        when(session.getToken()).thenReturn("testToken2");

        final Notification notification = new LoginNotification(session);

        final NotificationHandler handler = mock(NotificationHandler.class);
        when(handler.getType()).thenReturn(Long.MAX_VALUE);
        when(handler.from()).thenReturn(session);

        try {
            notifier.register(handler);
            final Future<Boolean> result = notifier.sendWithResult(notification);
            assertFalse(result.get());
        } catch (UnsupportedNotificationException ok) {

        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void registerHandlerAndSendNotification_unregisterAndSendAgain_shouldRaiseExceptions() {
        final SimpleNotifier notifier = new SimpleNotifier(executor);
        final Session session = mock(Session.class);
        when(session.getToken()).thenReturn("testToken2");

        final Notification notification = new LoginNotification(session);

        final NotificationHandler handler = mock(NotificationHandler.class);
        when(handler.getType()).thenReturn(LoginNotification.NOTIFICATION_TYPE);
        when(handler.from()).thenReturn(session);

        try {
            notifier.register(handler);
            final Future<Boolean> result = notifier.sendWithResult(notification);
            assertTrue(result.get());
        } catch (UnsupportedNotificationException | InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }

        try {
            notifier.unregister(handler);
            final Future<Boolean> result = notifier.sendWithResult(notification);
            assertFalse(result.get());
        } catch (UnsupportedNotificationException ok) {

        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

}
