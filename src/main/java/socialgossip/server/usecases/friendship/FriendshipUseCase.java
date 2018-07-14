package socialgossip.server.usecases.friendship;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.UseCase;

public interface FriendshipUseCase
        extends UseCase<FriendshipUseCase.Input, FriendshipOutput>, ProtectedResource {
    interface Input extends PreAuthInput {
        String getFriendUsername();
    }
}
