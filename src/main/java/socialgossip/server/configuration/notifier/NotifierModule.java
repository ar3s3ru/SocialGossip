package socialgossip.server.configuration.notifier;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.notifier.SimpleNotifier;

import java.util.concurrent.ThreadPoolExecutor;

@Module
public class NotifierModule {
    @Provides
    @NotifierScope
    static Notifier provideNotifier(final ThreadPoolExecutor executor) {
        return new SimpleNotifier(executor);
    }
}
