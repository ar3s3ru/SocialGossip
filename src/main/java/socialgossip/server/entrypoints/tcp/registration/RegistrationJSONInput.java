package socialgossip.server.entrypoints.tcp.registration;

import org.json.simple.JSONObject;
import socialgossip.server.entrypoints.tcp.JSONInput;
import socialgossip.server.usecases.registration.RegistrationUseCase;
import socialgossip.server.validation.ValidationException;

import java.util.Optional;

public final class RegistrationJSONInput extends JSONInput implements RegistrationUseCase.Input {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String LANGUAGE_FIELD = "language";

    private final String username;
    private final String password;
    private final String language;

    public RegistrationJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
        this.username = (String) jsonObject.get(USERNAME_FIELD);
        this.password = (String) jsonObject.get(PASSWORD_FIELD);
        this.language = (String) jsonObject.get(LANGUAGE_FIELD);
    }

    @Override
    public void validate() throws ValidationException {
        Optional.ofNullable(username).orElseThrow(() -> new ValidationException(USERNAME_FIELD, "can't be null"));
        Optional.ofNullable(password).orElseThrow(() -> new ValidationException(PASSWORD_FIELD, "can't be null"));
        Optional.ofNullable(language).orElseThrow(() -> new ValidationException(LANGUAGE_FIELD, "can't be null"));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getLanguage() {
        return language;
    }
}
