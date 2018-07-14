package socialgossip.server.usecases.login;

import socialgossip.server.usecases.ErrorsHandler;
import socialgossip.server.usecases.UseCase;

/**
 * Use-case that allows the creation of new
 * {@link socialgossip.server.core.entities.session.Session} by login,
 * using an {@link socialgossip.server.core.entities.user.User} credentials.
 *
 * @param <O> is the use-case result type.
 * @param <E> is the {@link ErrorsHandler} object that allows to handle error cases
 *            in the use-case execution.
 */
public interface LoginUseCase<O, E extends ErrorsHandler>
        extends UseCase<LoginUseCase.Input, O, E> {
    /**
     * Represents the input of the login use-case
     */
    interface Input extends UseCase.Input {
        String getUsername();
        String getPassword();
    }
}
