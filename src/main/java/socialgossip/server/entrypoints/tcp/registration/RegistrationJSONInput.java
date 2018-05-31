package socialgossip.server.entrypoints.tcp.registration;

import org.json.simple.JSONObject;
import socialgossip.server.entrypoints.tcp.JSONInput;
import socialgossip.server.usecases.registration.RegistrationUseCase;

public final class RegistrationJSONInput extends JSONInput implements RegistrationUseCase.Input {
    private final String username;
    private final String password;
    private final String language;

    public RegistrationJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
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
}
