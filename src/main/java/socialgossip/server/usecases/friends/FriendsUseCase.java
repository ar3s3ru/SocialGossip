package socialgossip.server.usecases.friends;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.UseCase;
import socialgossip.server.usecases.friendship.FriendshipOutput;

import java.util.List;

public interface FriendsUseCase
        extends UseCase<FriendsUseCase.Input, List<FriendshipOutput>>, ProtectedResource {
    interface Input extends PreAuthInput{}
}
