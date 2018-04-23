package socialgossip.server.core.gateways.notifications;

import socialgossip.server.core.entities.session.SessionScoped;

public interface Notification extends SessionScoped {
    long getType();
}
