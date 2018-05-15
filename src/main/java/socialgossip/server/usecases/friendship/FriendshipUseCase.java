package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.UseCase;

public interface FriendshipUseCase<O, E extends ProtectedErrorsHandler>
        extends UseCase<FriendshipUseCase.Input, O, E>, ProtectedResource {
    interface Input extends PreAuthInput {
        String getFriendUsername();
    }
}
