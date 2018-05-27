package socialgossip.server.configuration.application;

import dagger.Module;
import dagger.Provides;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Module
public class ApplicationModule {
    @Provides
    public static ThreadPoolExecutor provideThreadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }
}
