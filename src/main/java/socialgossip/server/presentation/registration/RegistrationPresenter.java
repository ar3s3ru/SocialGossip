package socialgossip.server.presentation.registration;

import org.json.simple.JSONObject;
import socialgossip.server.presentation.Presenter;

public final class RegistrationPresenter implements Presenter<String> {
    @Override
    public JSONObject getOkResponse(final String result) {
        final JSONObject jsonObject = Presenter.super.getOkResponse(result);
        jsonObject.put("username", result);
        return jsonObject;
    }
}
