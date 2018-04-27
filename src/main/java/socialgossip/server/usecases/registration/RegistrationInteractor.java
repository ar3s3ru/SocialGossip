package socialgossip.server.usecases.registration;

import socialgossip.server.core.entities.password.EncryptedPassword;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.AddUserAccess;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.usecases.AbstractUseCase;
import socialgossip.server.usecases.logging.UseCaseLogger;

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
            final Locale lang = produceLanguageLocale(input);
            final EncryptedPassword<?> password = produceEncryptedPassword(input);
            final User user = produceNewUser(input, lang, password);
            onSuccess.accept(trySavingNewUser(input, user));
        } catch (IllformedLocaleException e) {
            UseCaseLogger.error(LOG, input, () -> "IllformedLocaleException: " + e.getMessage());
            errors.onInvalidLanguage(e);
        } catch (InvalidPasswordException e) {
            UseCaseLogger.error(LOG, input, () -> "InvalidPasswordException : " + e.getMessage());
            errors.onInvalidPassword(e);
        } catch (InvalidUserException e) {
            UseCaseLogger.error(LOG, input, () -> "InvalidUserException: " + e.getMessage());
            errors.onInvalidUser(e);
        } catch (UserAlreadyExistsException e) {
            UseCaseLogger.error(LOG, input, () -> "UserAlreadyExistsException: " + e.getMessage());
            errors.onUserAlreadyExists(e);
        } catch (GatewayException e) {
            UseCaseLogger.error(LOG, input, () -> "GatewayError: " + e.getMessage());
            errors.onGatewayError(e);
        }
    }

    private Locale produceLanguageLocale(final RegistrationUseCase.Input input)
            throws IllformedLocaleException {
        final Locale lang = localeFactory.produce().setLanguageTag(input.getLanguage()).build();
        UseCaseLogger.fine(LOG, input, () -> "produced new language: " + input.getLanguage());
        return lang;
    }

    private EncryptedPassword<?> produceEncryptedPassword(final RegistrationUseCase.Input input)
            throws InvalidPasswordException {
        final EncryptedPassword<?> password = encryptionSchema.from(input.getPassword());
        UseCaseLogger.fine(LOG, input, () -> "encrypted password successfully");
        return password;
    }

    private User produceNewUser(final RegistrationUseCase.Input input,
                                final Locale                    lang,
                                final EncryptedPassword<?>      password)
            throws InvalidUserException {
        final User user = userFactory.produce(input.getUsername(), lang, password);
        UseCaseLogger.fine(LOG, input, () -> "produced new User: " + user);
        return user;
    }

    private boolean trySavingNewUser(final RegistrationUseCase.Input input,
                                     final User newUser)
            throws UserAlreadyExistsException, GatewayException {
        UseCaseLogger.fine(LOG, input, () -> "adding user to persistence layer...");
        final boolean result = userGateway.add(newUser);
        UseCaseLogger.info(LOG, input, () -> "User \"" + newUser.getId() + "\" add operation result: " + result);
        return result;
    }
}
