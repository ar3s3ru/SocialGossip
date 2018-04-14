package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.password.Password;

@FunctionalInterface
public interface PasswordFactory {
    Password produceNewPasswordFrom(String password) throws EmptyPasswordException;
}
