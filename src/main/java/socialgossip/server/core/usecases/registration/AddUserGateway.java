package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

public interface AddUserGateway {
    boolean add(User user) throws UserAlreadyExistsException, GatewayException;
}
