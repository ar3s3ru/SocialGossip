package socialgossip.server.core.usecases;

import socialgossip.server.core.gateways.GatewayException;

public interface GatewayErrorsHandler extends ErrorsHandler {
    void onGatewayError(GatewayException e);
}
