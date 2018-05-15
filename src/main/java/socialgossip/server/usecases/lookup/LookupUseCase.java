package socialgossip.server.usecases.lookup;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.UseCase;

public interface LookupUseCase<O, E extends ProtectedErrorsHandler>
        extends UseCase<LookupUseCase.Input, O, E>, ProtectedResource {
    interface Input extends PreAuthInput {
        String getLookupUsername();
    }
}
