package socialgossip.server.usecases.login;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.notifications.UnsupportedNotificationException;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserNotFoundException;
import socialgossip.server.usecases.AbstractUseCase;
import socialgossip.server.usecases.logging.UseCaseLogger;

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

    private final UserFinder userAccess;
    private final SessionInserter   sessionInserter;
    private final PasswordValidator passwordValidator;
    private final SessionFactory    sessionFactory;

    private final Notifier                 notifier;
    private final LoginNotificationFactory notificationFactory;

    public LoginInteractor(final UserFinder userAccess,
                           final SessionInserter   sessionInserter,
                           final PasswordValidator passwordValidator,
                           final SessionFactory    sessionFactory,
                           final Notifier          notifier) {
        this(userAccess, sessionInserter, passwordValidator, sessionFactory, notifier, LoginNotification::new);
    }

    public LoginInteractor(final UserFinder userAccess,
                           final SessionInserter          sessionInserter,
                           final PasswordValidator        passwordValidator,
                           final SessionFactory           sessionFactory,
                           final Notifier                 notifier,
                           final LoginNotificationFactory notificationFactory) {
        this.userAccess          = Objects.requireNonNull(userAccess);
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
            UseCaseLogger.exception(LOG, input, e);
            errors.onUserNotFound(e);
        } catch (InvalidPasswordException e) {
            UseCaseLogger.exception(LOG, input, e);
            errors.onInvalidPassword(e);
        } catch (SessionAlreadyExistsException e) {
            UseCaseLogger.exception(LOG, input, e);
            errors.onSessionAlreadyExists(e);
        } catch (GatewayException e) {
            UseCaseLogger.exception(LOG, input, e);
            errors.onGatewayError(e);
        }
    }

    private User retrieveUserByUsername(final LoginUseCase.Input input)
            throws UserNotFoundException, GatewayException {
        UseCaseLogger.fine(LOG, input, () -> "retrieving user: " + input.getUsername());
        final User user = userAccess.findByUsername(input.getUsername());
        UseCaseLogger.fine(LOG, input, () -> "retrieved user: " + user);
        return user;
    }

    private void validateInputPassword(final LoginUseCase.Input input, final User user)
            throws InvalidPasswordException {
        UseCaseLogger.fine(LOG, input, () -> "validating input password format...");
        passwordValidator.validate(input.getPassword());

        UseCaseLogger.fine(LOG, input, () -> "check input password coherence with retrieved user...");
        if (!user.getPassword().verify(input.getPassword())) {
            throw new InvalidPasswordException(input.getPassword(), "passwords mismatch");
        }
        UseCaseLogger.fine(LOG, input, () -> "passwords match!");
    }

    private Session createAndAddNewSession(final LoginUseCase.Input input, final User user)
            throws SessionAlreadyExistsException, GatewayException{
        final Session session = sessionFactory.produce(user, input.getIpAddress());
        UseCaseLogger.info(LOG, input, () -> "produced new Session: " + session);

        UseCaseLogger.fine(LOG, input, () -> "writing to repository...");
        sessionInserter.insert(session);
        UseCaseLogger.info(LOG, input, () -> "Session successfully added to repository");
        return session;
    }

    private void tryRegisteringAndSendingLoginNotification(final LoginUseCase.Input input, final Session session) {
        try {
            UseCaseLogger.fine(LOG, input, () -> "registering Friendships notification handler...");
            notifier.register(input.getFriendshipsHandler().apply(session));
            UseCaseLogger.fine(LOG, input, () -> "sending login notification...");
            notifier.send(notificationFactory.produce(session));
        } catch (UnsupportedNotificationException e) {
            UseCaseLogger.warn(LOG, input, () -> "UnsupportedNotificationException: " + e.getMessage());
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
