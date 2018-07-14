package socialgossip.server.presentation.logout;

import org.json.simple.JSONObject;
import socialgossip.server.presentation.AbstractPresenter;

public final class LogoutPresenter extends AbstractPresenter<Void> {
    @Override
    public JSONObject getOkResponse(final Void result) {
        final JSONObject jsonObject = super.getOkResponse(result);
        jsonObject.put("message", "bye bye!");
        return jsonObject;
    }
}
