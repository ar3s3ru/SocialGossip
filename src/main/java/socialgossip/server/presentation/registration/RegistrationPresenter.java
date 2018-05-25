package socialgossip.server.presentation.registration;

import org.json.simple.JSONObject;
import socialgossip.server.presentation.AbstractPresenter;

public final class RegistrationPresenter extends AbstractPresenter<String> {
    @Override
    public JSONObject getOkResponse(final String result) {
        final JSONObject jsonObject = super.getOkResponse(result);
        jsonObject.put("username", result);
        return jsonObject;
    }
}
