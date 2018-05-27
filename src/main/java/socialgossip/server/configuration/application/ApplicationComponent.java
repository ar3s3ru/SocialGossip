package socialgossip.server.configuration.application;

import dagger.Component;
import socialgossip.server.configuration.dataproviders.DataproviderComponent;
import socialgossip.server.configuration.security.SecurityComponent;

import java.util.concurrent.ThreadPoolExecutor;

@Component(
        modules = ApplicationModule.class,
        dependencies = {
                SecurityComponent.class,
                DataproviderComponent.class
        }
)
public interface ApplicationComponent {
    ThreadPoolExecutor executor();
}
