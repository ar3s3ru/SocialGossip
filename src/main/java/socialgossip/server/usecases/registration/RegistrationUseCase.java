package socialgossip.server.usecases.registration;

import socialgossip.server.usecases.UseCase;

/**
 * Represents a new {@link socialgossip.server.core.entities.user.User}
 * registration {@link UseCase}.
 */
public interface RegistrationUseCase extends UseCase<RegistrationUseCase.Input, Boolean> {
    /**
     * Represents the input of the registration use-case.
     */
    interface Input extends UseCase.Input {
        String getUsername();
        String getPassword();
        String getLanguage();
    }
}
