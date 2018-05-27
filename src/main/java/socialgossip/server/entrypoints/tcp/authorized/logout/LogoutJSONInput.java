package socialgossip.server.entrypoints.tcp.authorized.logout;

import org.json.simple.JSONObject;
import socialgossip.server.entrypoints.tcp.authorized.AuthorizedJSONInput;
import socialgossip.server.usecases.logout.LogoutUseCase;

public class LogoutJSONInput extends AuthorizedJSONInput implements LogoutUseCase.Input {
    public LogoutJSONInput(final String requestId, final JSONObject jsonObject) {
        super(requestId, jsonObject);
    }
}
