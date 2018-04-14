package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.usecases.ErrorsHandler;

public interface RegistrationErrors extends ErrorsHandler {
    void onInvalidPassword(InvalidPasswordException e);
    void onInvalidUser(InvalidUserException e);
    void onUserAlreadyExists(UserAlreadyExistsException e);
    void onGatewayError(GatewayException e);
}
