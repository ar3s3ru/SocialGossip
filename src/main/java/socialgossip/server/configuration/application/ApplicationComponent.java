package socialgossip.server.configuration.application;

import dagger.Component;
import socialgossip.server.configuration.security.BcryptModule;
import socialgossip.server.configuration.security.SecurityComponent;
import socialgossip.server.configuration.server.ServerComponent;
import socialgossip.server.configuration.server.TCPServerModule;
import socialgossip.server.usecases.login.SessionFactory;

import javax.inject.Singleton;
import java.util.concurrent.ThreadPoolExecutor;

@Component(modules = {FixedThreadPoolModule.class, SessionFactoryModule.class})
@Singleton
public interface ApplicationComponent {
    ThreadPoolExecutor executor();
    SessionFactory     sessionFactory();

    SecurityComponent attachSecurityComponent(BcryptModule module);
    ServerComponent   attachServerComponent(TCPServerModule module);
}
