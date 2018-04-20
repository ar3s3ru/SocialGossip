package socialgossip.server.core.gateways.user;

import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

public interface GetUserAccess {
    User getByUsername(String username) throws UserNotFoundException, GatewayException;
}
