package socialgossip.core.registration;

import socialgossip.server.core.usecases.UseCase;

public interface RegistrationUseCase<Input extends RegistrationInput, Output>
        extends UseCase<Input, Output> {
}
