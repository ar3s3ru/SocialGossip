package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.usecases.ErrorsHandler;

import java.util.IllformedLocaleException;

/**
 * Errors handler object that represents all the possible errors
 * from the execution of the {@link RegistrationInteractor}.
 */
public interface RegistrationErrors extends ErrorsHandler {
    void onInvalidLanguage(IllformedLocaleException e);
    void onInvalidPassword(InvalidPasswordException e);
    void onInvalidUser(InvalidUserException e);
    void onUserAlreadyExists(UserAlreadyExistsException e);
    void onGatewayError(GatewayException e);
}
