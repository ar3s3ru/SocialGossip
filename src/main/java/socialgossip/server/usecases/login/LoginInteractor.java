package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.AddSessionAccess;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.user.GetUserAccess;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.AbstractUseCase;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Implementation for {@link LoginUseCase}.
 */
public final class LoginInteractor
        extends AbstractUseCase<LoginUseCase.Input, LoginOutput, LoginErrors>
        implements LoginUseCase<LoginOutput, LoginErrors> {

    private final GetUserAccess     userAccess;
    private final AddSessionAccess  sessionAccess;
    private final PasswordValidator passwordValidator;
    private final SessionFactory    sessionFactory;

    private final Notifier                 notifier;
    private final LoginNotificationFactory notificationFactory;

    public LoginInteractor(final GetUserAccess            userAccess,
                           final AddSessionAccess         sessionAccess,
                           final PasswordValidator        passwordValidator,
                           final SessionFactory           sessionFactory,
                           final Notifier                 notifier,
                           final LoginNotificationFactory notificationFactory) {
        this.userAccess          = Objects.requireNonNull(userAccess);
        this.sessionAccess       = Objects.requireNonNull(sessionAccess);
        this.passwordValidator   = Objects.requireNonNull(passwordValidator);
        this.sessionFactory      = Objects.requireNonNull(sessionFactory);
        this.notifier            = Objects.requireNonNull(notifier);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    protected void onExecute(LoginUseCase.Input input, Consumer<LoginOutput> onSuccess, LoginErrors errors) {
        try {
            final User user = userAccess.getByUsername(input.getUsername());
            passwordValidator.validate(input.getPassword());
            if (user.getPassword().verify(input.getPassword())) {
                throw new InvalidPasswordException(input.getPassword(), "doesn't match");
            }
            final Session session = sessionFactory.produce(user, input.getIpAddress());
            sessionAccess.add(session);

            try {
                notifier.register(input.getFriendshipsHandler().apply(session));
                notifier.send(notificationFactory.produce(session));
            } catch (UnsupportedNotificationException e) {
                // TODO(ar3s3ru): add logging here
            } finally {
                onSuccess.accept(new LoginOutput(
                        session.getToken(),
                        session.getUser().getId(),
                        session.getExpireDate()
                ));
            }
        } catch (UserNotFoundException e) {
            errors.onUserNotFound(e);
        } catch (InvalidPasswordException e) {
            errors.onInvalidPassword(e);
        } catch (SessionAlreadyExistsException e) {
            errors.onSessionAlreadyExists(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }
}
