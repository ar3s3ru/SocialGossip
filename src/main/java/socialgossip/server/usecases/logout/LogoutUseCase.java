package socialgossip.server.usecases.logout;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.UseCase;

public interface LogoutUseCase
        extends UseCase<LogoutUseCase.Input, Boolean>, ProtectedResource {
    interface Input extends PreAuthInput {}
}
