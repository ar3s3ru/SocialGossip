package socialgossip.server.usecases.logout;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.UseCase;

public interface LogoutUseCase<O, E extends ProtectedErrorsHandler>
        extends UseCase<LogoutUseCase.Input, O, E>, ProtectedResource {
    interface Input extends PreAuthInput {}
}
