package socialgossip.server;

import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.session.SessionScoped;
import socialgossip.server.core.gateways.notifications.Notification;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.dataproviders.InMemoryRepository;
import socialgossip.server.entrypoints.tcp.TCPServer;
import socialgossip.server.entrypoints.tcp.login.LoginController;
import socialgossip.server.entrypoints.tcp.registration.RegistrationController;
import socialgossip.server.factories.session.UUIDv4SessionFactory;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.presentation.login.LoginPresenter;
import socialgossip.server.presentation.registration.RegistrationPresenter;
import socialgossip.server.security.BcryptSchema;
import socialgossip.server.security.SimplePasswordValidator;
import socialgossip.server.usecases.login.LoginInteractor;
import socialgossip.server.usecases.login.SessionFactory;
import socialgossip.server.usecases.registration.RegistrationInteractor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class Application {
    public static void main(String[] args) {
        final ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final TCPServer server = new TCPServer("tcp", 8080, executor);

        final PasswordValidator passwordValidator = new SimplePasswordValidator();
        final BcryptSchema bcryptSchema = new BcryptSchema(passwordValidator);

        final SessionFactory factory = new UUIDv4SessionFactory();

        final InMemoryRepository repository = new InMemoryRepository();
        final RegistrationInteractor interactor = new RegistrationInteractor(repository, bcryptSchema);
        final LoginInteractor interactor1 = new LoginInteractor(
                repository, repository, passwordValidator, factory, new Notifier() {
                    @Override
                    public void register(final NotificationHandler handler) {
                        AppLogger.fine(Logger.getGlobal(), null, () -> "register: " + handler);
                    }

                    @Override
                    public void unregister(final SessionScoped scope) {
                        AppLogger.fine(Logger.getGlobal(), null, () -> "unregister: " + scope);
                    }

                    @Override
                    public void send(final Notification notification) {
                        AppLogger.fine(Logger.getGlobal(), null, () -> "send: " + notification);
                    }
                });

        final RegistrationPresenter presenter = new RegistrationPresenter();
        final RegistrationController controller = new RegistrationController(presenter, interactor);

        final LoginPresenter presenter1 = new LoginPresenter();
        final LoginController controller1 = new LoginController(presenter1, interactor1);

        server.registerController("register", controller);
        server.registerController("login", controller1);

        AppLogger.info(Logger.getGlobal(), null, () -> "starting TCP server...");
        final Future future = executor.submit(server);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {

        }
    }
}
