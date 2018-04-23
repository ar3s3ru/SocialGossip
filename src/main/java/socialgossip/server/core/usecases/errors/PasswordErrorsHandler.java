package socialgossip.server.core.usecases.errors;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.usecases.ErrorsHandler;

/**
 * Error handler to be used when passwords have to be provided and verified.
 */
public interface PasswordErrorsHandler extends ErrorsHandler {
    void onInvalidPassword(InvalidPasswordException e);
}
