package socialgossip.server.presentation.login;

import org.json.simple.JSONObject;
import socialgossip.server.logging.ISO8601Formatter;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.login.LoginOutput;

public final class LoginPresenter implements Presenter<LoginOutput> {
    @Override
    public JSONObject getOkResponse(final LoginOutput result) {
        final JSONObject jsonObject = Presenter.super.getOkResponse(result);
        jsonObject.put("token", result.getToken());
        jsonObject.put("user", result.getUser());
        jsonObject.put("expiresAt", ISO8601Formatter.from(result.getExpiresAt()));
        return jsonObject;
    }
}
