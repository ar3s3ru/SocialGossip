package socialgossip.server.core.usecases.login;

import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.session.AddSessionAccess;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.user.GetUserAccess;
import socialgossip.server.core.usecases.AbstractUseCase;

import java.util.Objects;
import java.util.function.Consumer;

public final class LoginInteractor
        extends AbstractUseCase<LoginUseCase.Input, LoginOutput, LoginErrors>
        implements LoginUseCase<LoginOutput, LoginErrors> {

    private final GetUserAccess     userAccess;
    private final AddSessionAccess  sessionAccess;
    private final PasswordValidator passwordValidator;
    private final SessionFactory    sessionFactory;

    public LoginInteractor(final GetUserAccess userAccess,
                           final AddSessionAccess sessionAccess,
                           final PasswordValidator passwordValidator,
                           final SessionFactory sessionFactory) {
        this.userAccess        = Objects.requireNonNull(userAccess);
        this.sessionAccess     = Objects.requireNonNull(sessionAccess);
        this.passwordValidator = Objects.requireNonNull(passwordValidator);
        this.sessionFactory    = Objects.requireNonNull(sessionFactory);
    }

    @Override
    protected void onExecute(Input input, Consumer<LoginOutput> onSuccess, LoginErrors errors) {
        try {
            final User user = userAccess.getByUsername(input.getUsername());
            passwordValidator.validate(input.getPassword());
            if (user.getPassword().verify(input.getPassword())) {
                throw new InvalidPasswordException(input.getPassword(), "doesn't match");
            }
            final Session session = sessionFactory.produce(user);
            sessionAccess.add(session);
            onSuccess.accept(new LoginOutput(
                    session.getToken(),
                    session.getUser().getId(),
                    session.getExpireDate()
            ));
        } catch (InvalidPasswordException e) {
            errors.onInvalidPassword(e);
        } catch (SessionAlreadyExistsException e) {
            errors.onSessionAlreadyExists(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }
}
