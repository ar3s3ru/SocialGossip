package socialgossip.server.core.usecases.registration;

import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.AddUserAccess;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.core.usecases.AbstractUseCase;
import socialgossip.server.core.usecases.UseCase;
import socialgossip.server.core.usecases.logging.UseCaseLogger;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Implementation for the {@link RegistrationUseCase}.
 */
public final class RegistrationInteractor
        extends AbstractUseCase<RegistrationUseCase.Input, Boolean, RegistrationErrors>
        implements RegistrationUseCase<Boolean, RegistrationErrors> {

    private final static Logger LOG = Logger.getLogger(RegistrationInteractor.class.getName());

    private final AddUserAccess        userGateway;
    private final EncryptionSchema<?>  encryptionSchema;
    private final UserFactory          userFactory;
    private final LocaleBuilderFactory localeFactory;

    /**
     * Creates a new instance of {@link RegistrationInteractor} using the default
     * {@link User#User(String, Locale, Password)} constructor as {@link UserFactory} and
     * {@link Locale.Builder#Builder()} constructor as {@link LocaleBuilderFactory}.
     *
     * For more documentation, see {@link RegistrationInteractor#RegistrationInteractor(
     * UserFactory, AddUserAccess, EncryptionSchema, LocaleBuilderFactory)}.
     */
    public RegistrationInteractor(final AddUserAccess       userGateway,
                                  final EncryptionSchema<?> encryptionSchema) {
        this(User::new, userGateway, encryptionSchema, Locale.Builder::new);
    }

    /**
     * Creates a new instance of {@link RegistrationInteractor} using the default
     * {@link Locale.Builder} constructor as {@link LocaleBuilderFactory} result.
     *
     * For more documentation, see {@link RegistrationInteractor#RegistrationInteractor(
     * UserFactory, AddUserAccess, EncryptionSchema, LocaleBuilderFactory)}.
     */
    public RegistrationInteractor(final UserFactory         userFactory,
                                  final AddUserAccess       userGateway,
                                  final EncryptionSchema<?> encryptionSchema) {
        this(userFactory, userGateway, encryptionSchema, Locale.Builder::new);
    }

    /**
     * Creates a new instance of a {@link RegistrationInteractor}, that implements
     * the {@link RegistrationUseCase} logic.
     * @param userFactory is the factory object used to create new {@link User} objects.
     * @param userGateway is the data access object used to add new {@link User} to the
     *                    persistence layer of the application.
     * @param encryptionSchema is the encryption algorithm used to encrypt
     *                        the plain-text password chosen for the new {@link User}.
     */
    public RegistrationInteractor(final UserFactory          userFactory,
                                  final AddUserAccess        userGateway,
                                  final EncryptionSchema<?>  encryptionSchema,
                                  final LocaleBuilderFactory localeBuilderFactory) {
        this.userGateway      = Objects.requireNonNull(userGateway);
        this.encryptionSchema = Objects.requireNonNull(encryptionSchema);
        this.userFactory      = Objects.requireNonNull(userFactory);
        this.localeFactory    = Objects.requireNonNull(localeBuilderFactory);
    }

    @Override
    protected void onExecute(final RegistrationUseCase.Input input,
                             final Consumer<Boolean>         onSuccess,
                             final RegistrationErrors        errors) {
        try {
            final Locale lang = localeFactory.produce().setLanguageTag(input.getLanguage()).build();
            UseCaseLogger.fine(LOG, input, () -> "produced new language: " + input.getLanguage());
            final Password password = encryptionSchema.from(input.getPassword());
            UseCaseLogger.fine(LOG, input, () -> "encrypted password successfully");
            final User user = userFactory.produce(input.getUsername(), lang, password);
            UseCaseLogger.fine(LOG, input, () -> "produced new User: " + user);
            final boolean result = userGateway.add(user);
            UseCaseLogger.fine(LOG, input, () -> "adding user to persistence layer returned: " + result);
            onSuccess.accept(result);
        } catch (IllformedLocaleException e) {
            UseCaseLogger.info(LOG, input, () -> "IllformedLocaleException: " + e.getMessage());
            errors.onInvalidLanguage(e);
        } catch (InvalidPasswordException e) {
            UseCaseLogger.info(LOG, input, () -> "InvalidPasswordException : " + e.getMessage());
            errors.onInvalidPassword(e);
        } catch (InvalidUserException e) {
            UseCaseLogger.fine(LOG, input, () -> "InvalidUserException: " + e.getMessage());
            errors.onInvalidUser(e);
        } catch (UserAlreadyExistsException e) {
            UseCaseLogger.warn(LOG, input, () -> "UserAlreadyExistsException: " + e.getMessage());
            errors.onUserAlreadyExists(e);
        } catch (GatewayException e) {
            UseCaseLogger.error(LOG, input, () -> "GatewayError: " + e.getMessage());
            errors.onGatewayError(e);
        }
    }
}
