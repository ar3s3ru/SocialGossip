package socialgossip.server.core.gateways.session;

import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;

public interface AddSessionAccess {
    void add(Session session) throws SessionAlreadyExistsException, GatewayException;
}
