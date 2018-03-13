package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.EmptyPasswordException;
import socialgossip.server.core.entities.InvalidUserIdException;
import socialgossip.server.core.entities.Password;
import socialgossip.server.core.entities.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.usecases.UseCase;

import java.util.Locale;
import java.util.Objects;

public class RegistrationInteractor extends UseCase<RegistrationInput, Boolean> {
    private final AddUserGateway   userGateway;
    private final Password.Factory passwordFactory;
    private final User.Factory     userFactory;

    public RegistrationInteractor(final AddUserGateway   userGateway,
                                  final Password.Factory passwordFactory,
                                  final User.Factory     userFactory) {
        this.userGateway     = Objects.requireNonNull(userGateway);
        this.passwordFactory = Objects.requireNonNull(passwordFactory);
        this.userFactory     = Objects.requireNonNull(userFactory);
    }

    @Override
    public void execute(final RegistrationInput input, final Callback<Boolean> callback) {
        try {
            final Locale locale     = Locale.forLanguageTag(input.getLang());
            final Password password = passwordFactory.produceNewPasswordFrom(input.getPassword());
            final User user         = userFactory.produceWith(input.getId(), locale, password);
            callback.onSuccess(userGateway.add(user));
        } catch (EmptyPasswordException | InvalidUserIdException | UserAlreadyExistsException | GatewayException e) {
            callback.onError(e);
        }
    }
}
