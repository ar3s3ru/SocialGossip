package socialgossip.server.usecases.login;

import socialgossip.server.usecases.UseCase;

/**
 * Use-case that allows the creation of new
 * {@link socialgossip.server.core.entities.session.Session} by login,
 * using an {@link socialgossip.server.core.entities.user.User} credentials.
 */
public interface LoginUseCase extends UseCase<LoginUseCase.Input, LoginOutput> {
    /**
     * Represents the input of the login use-case
     */
    interface Input extends UseCase.Input {
        String getUsername();
        String getPassword();
    }
}
