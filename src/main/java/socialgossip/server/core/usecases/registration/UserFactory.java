package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;

import java.util.Locale;

/**
 * Factory for new {@link User} objects, used by the {@link RegistrationUseCase} implementation.
 */
@FunctionalInterface
public interface UserFactory {
    User produce(String username, Locale lang, Password password) throws InvalidUserException;
}
