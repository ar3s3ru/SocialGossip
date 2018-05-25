package socialgossip.server.entrypoints.tcp.registration;

import org.json.simple.JSONObject;
import socialgossip.server.usecases.registration.RegistrationUseCase;

import java.util.Objects;

public final class RegistrationJSONInput implements RegistrationUseCase.Input {
    private final String username;
    private final String password;
    private final String language;
    private final String requestId;

    public RegistrationJSONInput(final String requestId, final JSONObject jsonObject) {
        this.requestId = requestId;
        Objects.requireNonNull(jsonObject);
        this.username = (String) jsonObject.get("username");
        this.password = (String) jsonObject.get("password");
        this.language = (String) jsonObject.get("language");
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

    @Override
    public String getRequestId() {
        return requestId;
    }
}
