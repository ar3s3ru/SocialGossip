package socialgossip.server.configuration.notifier;

import dagger.Subcomponent;
import socialgossip.server.core.gateways.notifications.Notifier;

@Subcomponent(modules = NotifierModule.class)
@NotifierScope
public interface NotifierComponent {
    Notifier notifier();
}
