package socialgossip.server;

import socialgossip.server.configuration.application.ApplicationComponent;
import socialgossip.server.configuration.application.DaggerApplicationComponent;
import socialgossip.server.configuration.dataproviders.DaggerDataproviderComponent;
import socialgossip.server.configuration.dataproviders.DataproviderComponent;
import socialgossip.server.configuration.security.DaggerSecurityComponent;
import socialgossip.server.configuration.security.SecurityComponent;
import socialgossip.server.core.entities.session.SessionScoped;
import socialgossip.server.core.gateways.notifications.Notification;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.entrypoints.tcp.TCPServer;
import socialgossip.server.entrypoints.tcp.login.LoginController;
import socialgossip.server.entrypoints.tcp.registration.RegistrationController;
import socialgossip.server.factories.session.UUIDv4SessionFactory;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.presentation.login.LoginPresenter;
import socialgossip.server.presentation.registration.RegistrationPresenter;
import socialgossip.server.usecases.login.LoginInteractor;
import socialgossip.server.usecases.login.SessionFactory;
import socialgossip.server.usecases.registration.RegistrationInteractor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

class Application {
    private final ApplicationComponent  appComponent;
    private final SecurityComponent     securityComponent;
    private final DataproviderComponent dataproviderComponent;

    Application() {
        securityComponent     = DaggerSecurityComponent.create();
        dataproviderComponent = DaggerDataproviderComponent.create();

        appComponent = DaggerApplicationComponent
                .builder()
                .securityComponent(securityComponent)
                .dataproviderComponent(dataproviderComponent)
                .build();
    }

    void start() {
        final TCPServer server = new TCPServer("tcp", 8080, appComponent.executor());

        final SessionFactory factory = new UUIDv4SessionFactory();

        final RegistrationInteractor interactor = new RegistrationInteractor(
                        dataproviderComponent.userRepository(),
                        securityComponent.encryptionSchema()
        );

        final LoginInteractor interactor1 = new LoginInteractor(
                dataproviderComponent.userRepository(),
                dataproviderComponent.sessionRepository(),
                securityComponent.passwordValidator(),
                factory,
                new Notifier() {
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
        final Future future = appComponent.executor().submit(server);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {

        }
    }
}
