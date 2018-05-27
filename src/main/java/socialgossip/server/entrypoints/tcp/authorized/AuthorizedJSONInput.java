package socialgossip.server.entrypoints.tcp.authorized;

import org.json.simple.JSONObject;
import socialgossip.server.entrypoints.tcp.JSONInput;
import socialgossip.server.usecases.PreAuthInput;

public class AuthorizedJSONInput extends JSONInput implements PreAuthInput {
    protected final String sessionToken;

    protected AuthorizedJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
        this.sessionToken = (String) jsonObject.get("token");
    }

    @Override
    public String getSessionToken() {
        return sessionToken;
    }
}
