package socialgossip.server.usecases.registration;

import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.usecases.errors.GatewayErrorsHandler;
import socialgossip.server.usecases.errors.PasswordErrorsHandler;

import java.util.IllformedLocaleException;

/**
 * Errors handler object that represents all the possible errors
 * from the execution of the {@link RegistrationInteractor}.
 */
public interface RegistrationErrors extends GatewayErrorsHandler, PasswordErrorsHandler {
    void onInvalidLanguage(IllformedLocaleException e);
    void onInvalidUser(InvalidUserException e);
    void onUserAlreadyExists(UserAlreadyExistsException e);
}
