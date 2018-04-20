package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.usecases.ErrorsHandler;
import socialgossip.server.core.usecases.UseCase;

/**
 * Represents a new {@link socialgossip.server.core.entities.user.User}
 * registration {@link UseCase}.
 * @param <O> is the output type, produced after the use-case execution.
 * @param <E> is the {@link ErrorsHandler} that allows to handle error cases
 *            happened from the use-case execution.
 */
public interface RegistrationUseCase<O, E extends ErrorsHandler>
        extends UseCase<RegistrationUseCase.Input, O, E> {
    /**
     * Represents the input of the registration use-case.
     */
    interface Input extends UseCase.Input {
        String getUsername();
        String getPassword();
        String getLanguage();
    }
}
