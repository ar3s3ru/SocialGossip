package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.usecases.UseCase;

public interface RegistrationUseCase<O, E>
        extends UseCase<RegistrationUseCase.Input, O, E> {
    interface Input {
        String getUsername();
        String getPassword();
        String getLanguage();
    }
}
