package socialgossip.server.configuration.application;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Module
public class FixedThreadPoolModule {
    @Provides
    @Singleton static ThreadPoolExecutor provideThreadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }
}
