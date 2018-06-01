package socialgossip.server.entrypoints.tcp.authorized;

import org.json.simple.JSONObject;
import socialgossip.server.entrypoints.tcp.JSONInput;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.validation.ValidationException;

import java.util.Optional;

public class AuthorizedJSONInput extends JSONInput implements PreAuthInput {
    public static final String SESSION_TOKEN_FIELD = "token";

    protected final String sessionToken;

    protected AuthorizedJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
        this.sessionToken = (String) jsonObject.get(SESSION_TOKEN_FIELD);
    }

    @Override
    public void validate() throws ValidationException {
        Optional.ofNullable(sessionToken).orElseThrow(
                () -> new ValidationException(SESSION_TOKEN_FIELD, "can't be null")
        );
    }

    @Override
    public String getSessionToken() {
        return sessionToken;
    }
}
