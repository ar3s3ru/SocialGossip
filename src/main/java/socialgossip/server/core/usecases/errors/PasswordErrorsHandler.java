package socialgossip.server.core.usecases.errors;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.usecases.ErrorsHandler;

public interface PasswordErrorsHandler extends ErrorsHandler {
    void onInvalidPassword(InvalidPasswordException e);
}
