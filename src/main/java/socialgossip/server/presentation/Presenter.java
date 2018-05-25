package socialgossip.server.presentation;

import org.json.simple.JSONObject;

public interface Presenter<ResultType> {
    default JSONObject getOkResponse(ResultType result) {
        final JSONObject response = new JSONObject();
        response.put("result", "success");
        return response;
    }

    default JSONObject getFailResponse(Throwable fail) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "fail");
        jsonObject.put("message", fail.getMessage());
        return jsonObject;
    }

    default JSONObject getErrorResponse(Throwable error) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", "error");
        jsonObject.put("message", error.getMessage());
        return jsonObject;
    }
}
