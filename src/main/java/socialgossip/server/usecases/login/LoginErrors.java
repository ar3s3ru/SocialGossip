package socialgossip.server.usecases.login;

import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;
import socialgossip.server.usecases.errors.PasswordErrorsHandler;

/**
 * Errors handler for the {@link LoginUseCase} implementation.
 */
public interface LoginErrors extends GatewayErrorsHandler, PasswordErrorsHandler {
    void onUserNotFound(UserNotFoundException e);
    void onSessionAlreadyExists(SessionAlreadyExistsException e);
}
