package socialgossip.server;

import socialgossip.server.configuration.application.ApplicationComponent;
import socialgossip.server.configuration.application.DaggerApplicationComponent;
import socialgossip.server.configuration.controller.ControllerComponent;
import socialgossip.server.configuration.controller.DaggerControllerComponent;
import socialgossip.server.configuration.dataproviders.DataproviderComponent;
import socialgossip.server.configuration.dataproviders.inmemory.InMemoryModule;
import socialgossip.server.configuration.interactors.InteractorsComponent;
import socialgossip.server.configuration.interactors.LoginModule;
import socialgossip.server.configuration.interactors.LogoutModule;
import socialgossip.server.configuration.interactors.RegistrationModule;
import socialgossip.server.configuration.notifier.NotifierComponent;
import socialgossip.server.configuration.notifier.NotifierModule;
import socialgossip.server.configuration.presentation.PresentationComponent;
import socialgossip.server.configuration.presentation.PresentersModule;
import socialgossip.server.configuration.security.BcryptModule;
import socialgossip.server.configuration.security.SecurityComponent;
import socialgossip.server.configuration.server.ServerComponent;
import socialgossip.server.configuration.server.TCPServerModule;
import socialgossip.server.logging.AppLogger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

class Application {
    final ApplicationComponent  appComponent;
    final SecurityComponent     securityComponent;
    final DataproviderComponent dataproviderComponent;
//    final NotifierComponent     notifierComponent;
    final InteractorsComponent  interactorsComponent;
    final ServerComponent       serverComponent;
    final PresentationComponent presentationComponent;
    final ControllerComponent   controllerComponent;

    Application() {
        appComponent          = DaggerApplicationComponent.create();
        securityComponent     = appComponent.attachSecurityComponent(new BcryptModule());
//        notifierComponent     = appComponent.attachNotifierComponent(new NotifierModule());
        dataproviderComponent = securityComponent.attachDataproviderComponent(new InMemoryModule());
        serverComponent       = appComponent.attachServerComponent(new TCPServerModule("tcp", 8080));
        presentationComponent = serverComponent.attachPresentationComponent(new PresentersModule());

        interactorsComponent = dataproviderComponent.attachInteractorsComponent(
                new RegistrationModule(), new LoginModule(), new LogoutModule()
        );

        controllerComponent = DaggerControllerComponent.builder()
                                .interactorsComponent(interactorsComponent)
                                .presentationComponent(presentationComponent)
                                .build();

        serverComponent.tcpServer().registerController("register", controllerComponent.registrationController());
        serverComponent.tcpServer().registerController("login",    controllerComponent.loginController());
        serverComponent.tcpServer().registerController("logout",   controllerComponent.logoutController());
    }

    void start() {
        AppLogger.info(Logger.getGlobal(), null, () -> "starting TCP server...");
        final Future future = appComponent.executor().submit(serverComponent.tcpServer());
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            AppLogger.exception(Logger.getGlobal(), null, e);
        }
    }
}
