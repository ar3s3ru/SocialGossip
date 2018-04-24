package socialgossip.server.core.usecases.login;

import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.core.usecases.errors.GatewayErrorsHandler;
import socialgossip.server.core.usecases.errors.PasswordErrorsHandler;

/**
 * Errors handler for the {@link LoginUseCase} implementation.
 */
public interface LoginErrors extends GatewayErrorsHandler, PasswordErrorsHandler {
    void onUserNotFound(UserNotFoundException e);
    void onSessionAlreadyExists(SessionAlreadyExistsException e);
}
