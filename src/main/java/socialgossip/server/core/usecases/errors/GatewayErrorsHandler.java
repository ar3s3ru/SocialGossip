package socialgossip.server.core.usecases.errors;

import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.usecases.ErrorsHandler;

public interface GatewayErrorsHandler extends ErrorsHandler {
    void onGatewayError(GatewayException e);
}
