package socialgossip.server.usecases.friendship.notify;

import socialgossip.server.core.entities.auth.ProtectedResource;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.notifications.NotificationHandler;
import socialgossip.server.usecases.PreAuthInput;
import socialgossip.server.usecases.UseCase;

import java.util.function.Function;

public interface FriendshipNotifyUseCase<O, E extends ProtectedErrorsHandler>
        extends UseCase<FriendshipNotifyUseCase.Input, O, E>, ProtectedResource {
    interface Input extends PreAuthInput {
        Function<Session, NotificationHandler> handlerFactory();
    }
}
