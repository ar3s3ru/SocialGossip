package socialgossip.server.usecases.errors;

import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.usecases.ErrorsHandler;

/**
 * Error handler to be used when accessing a gateway that can cause error.
 */
public interface GatewayErrorsHandler extends ErrorsHandler {
    void onGatewayError(GatewayException e);
}