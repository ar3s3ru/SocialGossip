package socialgossip.server.presentation;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class AbstractPresenter<InputType> implements Presenter<InputType> {
    @Override
    public JSONObject getErrorResponse(final Throwable e) {
        final JSONObject jsonObject = Presenter.super.getErrorResponse(e);
        if (e instanceof ParseException) {
            jsonObject.put("message", e.toString());
        }
        return jsonObject;
    }
}
