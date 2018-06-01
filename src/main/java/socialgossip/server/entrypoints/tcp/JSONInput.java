package socialgossip.server.entrypoints.tcp;

import org.json.simple.JSONObject;
import socialgossip.server.usecases.UseCase;
import socialgossip.server.validation.Validable;

import java.util.Objects;

public abstract class JSONInput implements UseCase.Input, Validable {
    protected final String requestId;

    protected JSONInput(final String requestId, final JSONObject object) {
        this.requestId = Objects.requireNonNull(requestId);
        Objects.requireNonNull(object);
    }

    @Override
    public String getRequestId() {
        return requestId;
    }
}
