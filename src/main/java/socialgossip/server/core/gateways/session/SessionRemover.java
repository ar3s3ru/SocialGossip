package socialgossip.server.core.gateways.session;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;

public interface SessionRemover {
    void remove(Session session) throws SessionNotFoundException, GatewayException;
}
