package socialgossip.server.core.usecases.registration;

import socialgossip.core.registration.RegistrationInput;
import socialgossip.core.registration.RegistrationUseCase;
import socialgossip.server.core.entities.user.InvalidUserIdException;
import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.usecases.AbstractUseCase;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public final class RegistrationInteractor
        extends AbstractUseCase<RegistrationInput, Boolean>
        implements RegistrationUseCase<RegistrationInput, Boolean> {

    private final AddUserGateway  userGateway;
    private final PasswordFactory passwordFactory;
    private final User.Factory    userFactory;

    public RegistrationInteractor(final AddUserGateway  userGateway,
                                  final PasswordFactory passwordFactory,
                                  final User.Factory    userFactory) {
        this.userGateway     = Objects.requireNonNull(userGateway);
        this.passwordFactory = Objects.requireNonNull(passwordFactory);
        this.userFactory     = Objects.requireNonNull(userFactory);
    }

    @Override
    protected void onExecute(final RegistrationInput input,
                             final Consumer<Boolean> onSuccess,
                             final Consumer<Throwable> onError) {
        try {
            final Locale locale     = Locale.forLanguageTag(input.getLanguage());
            final Password password = passwordFactory.produceNewPasswordFrom(input.getPassword());
            final User user         = userFactory.produceWith(input.getUsername(), locale, password);
            onSuccess.accept(userGateway.add(user));
        } catch (EmptyPasswordException | InvalidUserIdException | UserAlreadyExistsException | GatewayException e) {
            onError.accept(e);
        }
    }
}
