package socialgossip.server.configuration.application;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.factories.session.UUIDv4SessionFactory;
import socialgossip.server.usecases.login.SessionFactory;

import javax.inject.Singleton;

@Module
public class SessionFactoryModule {
    @Provides
    @Singleton static SessionFactory provideSessionFactory() {
        return new UUIDv4SessionFactory();
    }
}
