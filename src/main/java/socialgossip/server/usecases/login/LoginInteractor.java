package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.AbstractUseCase;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Implementation for {@link LoginUseCase}.
 */
public final class LoginInteractor
        extends AbstractUseCase<LoginUseCase.Input, LoginOutput, LoginErrors>
        implements LoginUseCase<LoginOutput, LoginErrors> {

    private final static Logger LOG = Logger.getLogger(LoginInteractor.class.getName());

    private final UserFinder        userFinder;
    private final SessionInserter   sessionInserter;
    private final PasswordValidator passwordValidator;
    private final SessionFactory    sessionFactory;

    private final Notifier                 notifier;
    private final LoginNotificationFactory notificationFactory;

    public LoginInteractor(final UserFinder        userFinder,
                           final SessionInserter   sessionInserter,
                           final PasswordValidator passwordValidator,
                           final SessionFactory    sessionFactory,
                           final Notifier          notifier) {
        this(userFinder, sessionInserter, passwordValidator, sessionFactory, notifier, LoginNotification::new);
    }

    public LoginInteractor(final UserFinder               userFinder,
                           final SessionInserter          sessionInserter,
                           final PasswordValidator        passwordValidator,
                           final SessionFactory           sessionFactory,
                           final Notifier                 notifier,
                           final LoginNotificationFactory notificationFactory) {
        this.userFinder          = Objects.requireNonNull(userFinder);
        this.sessionInserter     = Objects.requireNonNull(sessionInserter);
        this.passwordValidator   = Objects.requireNonNull(passwordValidator);
        this.sessionFactory      = Objects.requireNonNull(sessionFactory);
        this.notifier            = Objects.requireNonNull(notifier);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    protected void onExecute(LoginUseCase.Input input, Consumer<LoginOutput> onSuccess, LoginErrors errors) {
        try {
            final User user = retrieveUserByUsername(input);
            validateInputPassword(input, user);
            final Session session = createAndAddNewSession(input, user);
            tryRegisteringAndSendingLoginNotification(input, session);
            onSuccess.accept(produceNewOutput(session));
        } catch (UserNotFoundException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            errors.onUserNotFound(e);
        } catch (InvalidPasswordException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            errors.onInvalidPassword(e);
        } catch (SessionAlreadyExistsException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            errors.onSessionAlreadyExists(e);
        } catch (GatewayException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            errors.onGatewayError(e);
        }
    }

    private User retrieveUserByUsername(final LoginUseCase.Input input)
            throws UserNotFoundException, GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "retrieving user: " + input.getUsername());
        final User user = userFinder.findByUsername(input.getUsername());
        AppLogger.fine(LOG, input::getRequestId, () -> "retrieved user: " + user);
        return user;
    }

    private void validateInputPassword(final LoginUseCase.Input input, final User user)
            throws InvalidPasswordException {
        AppLogger.fine(LOG, input::getRequestId, () -> "validating input password format...");
        passwordValidator.validate(input.getPassword());

        AppLogger.fine(LOG, input::getRequestId, () -> "check input password coherence with retrieved user...");
        if (!user.getPassword().verify(input.getPassword())) {
            throw new InvalidPasswordException(input.getPassword(), "passwords mismatch");
        }
        AppLogger.fine(LOG, input::getRequestId, () -> "passwords match!");
    }

    private Session createAndAddNewSession(final LoginUseCase.Input input, final User user)
            throws SessionAlreadyExistsException, GatewayException{
        final Session session = sessionFactory.produce(user, input.getIpAddress());
        AppLogger.info(LOG, input::getRequestId, () -> "produced new Session: " + session);

        AppLogger.fine(LOG, input::getRequestId, () -> "writing to repository...");
        sessionInserter.insert(session);
        AppLogger.info(LOG, input::getRequestId, () -> "Session successfully added to repository");
        return session;
    }

    private void tryRegisteringAndSendingLoginNotification(final LoginUseCase.Input input, final Session session) {
        try {
            AppLogger.fine(LOG, input::getRequestId, () -> "registering Friendships notification handler...");
            notifier.register(input.getFriendshipsHandler().apply(session));
            AppLogger.fine(LOG, input::getRequestId, () -> "sending login notification...");
            notifier.send(notificationFactory.produce(session));
        } catch (UnsupportedNotificationException e) {
            AppLogger.warn(LOG, input::getRequestId, () -> "UnsupportedNotificationException: " + e.getMessage());
        }
    }

    private LoginOutput produceNewOutput(final Session session) {
        // Session expireDate should be present
        return new LoginOutput(
                session.getToken(),
                session.getUser().getId(),
                session.getExpireDate().orElseThrow(
                        () -> new RuntimeException("session must have expire date!")
                )
        );
    }
}
