package socialgossip.server.usecases.lookup;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.UseCase;

public interface LookupUseCase
        extends UseCase<LookupUseCase.Input, LookupDetails>, ProtectedResource {
    interface Input extends PreAuthInput {
        String getLookupUsername();
    }
}
