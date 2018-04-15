package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.password.EncryptedSchema;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.usecases.AbstractUseCase;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Implementation for the {@link RegistrationUseCase}.
 */
public final class RegistrationInteractor
        extends AbstractUseCase<RegistrationUseCase.Input, Boolean, RegistrationErrors>
        implements RegistrationUseCase<Boolean, RegistrationErrors> {

    private final AddUserDataAccess    userGateway;
    private final EncryptedSchema<?>   encryptedSchema;
    private final UserFactory          userFactory;
    private final LocaleBuilderFactory localeFactory;

    /**
     * Creates a new instance of {@link RegistrationInteractor} using the default
     * {@link User#User(String, Locale, Password)} constructor as {@link UserFactory} and
     * {@link Locale.Builder#Builder()} constructor as {@link LocaleBuilderFactory}.
     *
     * For more documentation, see {@link RegistrationInteractor#RegistrationInteractor(
     * UserFactory,AddUserDataAccess, EncryptedSchema, LocaleBuilderFactory)}.
     */
    public RegistrationInteractor(final AddUserDataAccess userGateway,
                                  final EncryptedSchema<?> encryptedSchema) {
        this(User::new, userGateway, encryptedSchema, Locale.Builder::new);
    }

    /**
     * Creates a new instance of {@link RegistrationInteractor} using the default
     * {@link Locale.Builder} constructor as {@link LocaleBuilderFactory} result.
     *
     * For more documentation, see {@link RegistrationInteractor#RegistrationInteractor(
     * UserFactory,AddUserDataAccess, EncryptedSchema, LocaleBuilderFactory)}.
     */
    public RegistrationInteractor(final UserFactory        userFactory,
                                  final AddUserDataAccess  userGateway,
                                  final EncryptedSchema<?> encryptedSchema) {
        this(userFactory, userGateway, encryptedSchema, Locale.Builder::new);
    }

    /**
     * Creates a new instance of a {@link RegistrationInteractor}, that implements
     * the {@link RegistrationUseCase} logic.
     * @param userFactory is the factory object used to create new {@link User} objects.
     * @param userGateway is the data access object used to add new {@link User} to the
     *                    persistence layer of the application.
     * @param encryptedSchema is the encryption algorithm used to encrypt
     *                        the plain-text password chosen for the new {@link User}.
     */
    public RegistrationInteractor(final UserFactory userFactory,
                                  final AddUserDataAccess userGateway,
                                  final EncryptedSchema<?> encryptedSchema,
                                  final LocaleBuilderFactory localeBuilderFactory) {
        this.userGateway     = Objects.requireNonNull(userGateway);
        this.encryptedSchema = Objects.requireNonNull(encryptedSchema);
        this.userFactory     = Objects.requireNonNull(userFactory);
        this.localeFactory   = Objects.requireNonNull(localeBuilderFactory);
    }

    @Override
    protected void onExecute(final RegistrationUseCase.Input input,
                             final Consumer<Boolean>         onSuccess,
                             final RegistrationErrors        errors) {
        // TODO(ar3s3ru): handle general Exceptions as well?
        try {
            onSuccess.accept(userGateway.add(
                    userFactory.produce(
                            input.getUsername(),
                            localeFactory.produce()
                                         .setLanguageTag(input.getLanguage())
                                         .build(),
                            encryptedSchema.from(input.getPassword())
                    )
            ));
        } catch (IllformedLocaleException e) {
            errors.onInvalidLanguage(e);
        } catch (InvalidPasswordException e) {
            errors.onInvalidPassword(e);
        } catch (InvalidUserException e) {
            errors.onInvalidUser(e);
        } catch (UserAlreadyExistsException e) {
            errors.onUserAlreadyExists(e);
        } catch (GatewayException e) {
            errors.onGatewayError(e);
        }
    }
}
