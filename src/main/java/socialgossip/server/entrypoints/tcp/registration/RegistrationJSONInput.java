package socialgossip.server.presentation.registration;

import socialgossip.server.usecases.registration.RegistrationUseCase;

public final class RegistrationJSONInput implements RegistrationUseCase.Input {
    private final String username;
    private final String password;
    private final String language;



    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getLanguage() {
        return null;
    }

    @Override
    public String getRequestId() {
        return null;
    }
}
