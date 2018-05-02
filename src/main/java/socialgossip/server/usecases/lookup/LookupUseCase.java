package socialgossip.server.usecases.lookup;

import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.UseCase;

public interface LookupUseCase
        extends UseCase<LookupUseCase.Input, LookupDetails, LookupErrors> {
    interface Input extends PreAuthInput {
        String getLookupUsername();
    }
}
