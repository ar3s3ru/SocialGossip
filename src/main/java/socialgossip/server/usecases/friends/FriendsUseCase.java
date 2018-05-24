package socialgossip.server.usecases.friends;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.ProtectedErrorsHandler;
import socialgossip.server.usecases.UseCase;

public interface FriendsUseCase<O, E extends ProtectedErrorsHandler>
        extends UseCase<FriendsUseCase.Input, O, E>, ProtectedResource {
    interface Input extends PreAuthInput{}
}
