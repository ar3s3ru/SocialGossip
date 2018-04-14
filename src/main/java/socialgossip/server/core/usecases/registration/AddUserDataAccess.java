package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;

public interface AddUserDataAccess {
    boolean add(User user) throws UserAlreadyExistsException, GatewayException;
}
