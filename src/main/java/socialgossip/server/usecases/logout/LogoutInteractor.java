package socialgossip.server.usecases.logout;

import socialgossip.server.core.entities.auth.Permission;
import socialgossip.server.core.entities.auth.UnauthorizedException;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.GetSessionAccess;
import socialgossip.server.core.gateways.session.RemoveSessionAccess;
import socialgossip.server.core.gateways.session.SessionNotFoundException;
import socialgossip.server.usecases.ProtectedUseCase;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class LogoutInteractor
    extends ProtectedUseCase<LogoutUseCase.Input, Boolean, LogoutErrors>
    implements LogoutUseCase {

    private final RemoveSessionAccess       sessionAccess;
    private final Notifier                  notifier;
    private final LogoutNotificationFactory notificationFactory;

    public LogoutInteractor(final GetSessionAccess          getSessionAccess,
                            final RemoveSessionAccess       removeSessionAccess,
                            final Notifier                  notifier,
                            final LogoutNotificationFactory notificationFactory) {
        super(getSessionAccess);
        this.sessionAccess       = Objects.requireNonNull(removeSessionAccess);
        this.notifier            = Objects.requireNonNull(notifier);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    public void checkAllowance(final Permission permission) throws UnauthorizedException {
        // We accept any type of Session here: this case, however, should never happen;
        // if a null Session has been returned by the ProtectedUseCase code, it's probably
        // a Gateway/Repository class problem.
        Optional.ofNullable(permission).orElseThrow(
                () -> new UnauthorizedException("<null>", "null tokens can't be authorized on logout")
        );
    }

    @Override
    protected void onAuthorizedExecute(final Session             session,
                                       final LogoutUseCase.Input input,
                                       final Consumer<Boolean>   onSuccess,
                                       final LogoutErrors        errors) {
        try {
            sessionAccess.remove(session);
            final LogoutNotification notification = notificationFactory.produce(session);
            try {
                notifier.send(notification);
            } catch (UnsupportedNotificationException e) {
                // TODO(ar3s3ru): log here, but proceed
            }
            notifier.unregister(notification); // LogoutNotification is Session-scoped!
            onSuccess.accept(true);
        } catch (SessionNotFoundException e) {
            errors.onSessionNotFound(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }
}
