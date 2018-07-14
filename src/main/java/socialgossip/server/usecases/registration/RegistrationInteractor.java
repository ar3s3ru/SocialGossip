package socialgossip.server.usecases.registration;

import socialgossip.server.core.entities.password.EncryptedPassword;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.usecases.AbstractUseCase;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Implementation for the {@link RegistrationUseCase}.
 */
public final class RegistrationInteractor
        extends AbstractUseCase<RegistrationUseCase.Input, Boolean>
        implements RegistrationUseCase {

    private final static Logger LOG = Logger.getLogger(RegistrationInteractor.class.getName());

    private final UserInserter         inserter;
    private final EncryptionSchema<?>  encryptionSchema;
    private final UserFactory          userFactory;
    private final LocaleBuilderFactory localeFactory;

    /**
     * Creates a new instance of {@link RegistrationInteractor} using the default
     * {@link User#User(String, Locale, Password)} constructor as {@link UserFactory} and
     * {@link Locale.Builder#Builder()} constructor as {@link LocaleBuilderFactory}.
     *
     * For more documentation, see {@link RegistrationInteractor#RegistrationInteractor(
     * UserFactory, UserInserter, EncryptionSchema, LocaleBuilderFactory)}.
     */
    public RegistrationInteractor(final UserInserter        inserter,
                                  final EncryptionSchema<?> encryptionSchema) {
        this(User::new, inserter, encryptionSchema, Locale.Builder::new);
    }

    /**
     * Creates a new instance of {@link RegistrationInteractor} using the default
     * {@link Locale.Builder} constructor as {@link LocaleBuilderFactory} result.
     *
     * For more documentation, see {@link RegistrationInteractor#RegistrationInteractor(
     * UserFactory, UserInserter, EncryptionSchema, LocaleBuilderFactory)}.
     */
    public RegistrationInteractor(final UserFactory         userFactory,
                                  final UserInserter        inserter,
                                  final EncryptionSchema<?> encryptionSchema) {
        this(userFactory, inserter, encryptionSchema, Locale.Builder::new);
    }

    /**
     * Creates a new instance of a {@link RegistrationInteractor}, that implements
     * the {@link RegistrationUseCase} logic.
     * @param userFactory is the factory object used to create new {@link User} objects.
     * @param inserter is the data access object used to insert new {@link User} to the
     *                    persistence layer of the application.
     * @param encryptionSchema is the encryption algorithm used to encrypt
     *                        the plain-text password chosen for the new {@link User}.
     */
    public RegistrationInteractor(final UserFactory          userFactory,
                                  final UserInserter         inserter,
                                  final EncryptionSchema<?>  encryptionSchema,
                                  final LocaleBuilderFactory localeBuilderFactory) {
        this.inserter         = Objects.requireNonNull(inserter);
        this.encryptionSchema = Objects.requireNonNull(encryptionSchema);
        this.userFactory      = Objects.requireNonNull(userFactory);
        this.localeFactory    = Objects.requireNonNull(localeBuilderFactory);
    }

    @Override
    protected void onExecute(final RegistrationUseCase.Input input,
                             final Consumer<Boolean>         onSuccess,
                             final Consumer<Throwable>       onError) {
        try {
            final Locale lang = produceLanguageLocale(input);
            final EncryptedPassword<?> password = produceEncryptedPassword(input);
            final User user = produceNewUser(input, lang, password);
            trySavingNewUser(input, user);
            onSuccess.accept(true);
        } catch (IllformedLocaleException | InvalidPasswordException   |
                InvalidUserException      | UserAlreadyExistsException | GatewayException e) {
            AppLogger.exception(LOG, input::getRequestId, e);
            onError.accept(e);
        }
    }

    private Locale produceLanguageLocale(final RegistrationUseCase.Input input)
            throws IllformedLocaleException {
        final Locale lang = localeFactory.produce().setLanguageTag(input.getLanguage()).build();
        AppLogger.fine(LOG, input::getRequestId, () -> "produced new language: " + input.getLanguage());
        return lang;
    }

    private EncryptedPassword<?> produceEncryptedPassword(final RegistrationUseCase.Input input)
            throws InvalidPasswordException {
        final EncryptedPassword<?> password = encryptionSchema.from(input.getPassword());
        AppLogger.fine(LOG, input::getRequestId, () -> "encrypted password successfully");
        return password;
    }

    private User produceNewUser(final RegistrationUseCase.Input input,
                                final Locale                    lang,
                                final EncryptedPassword<?>      password)
            throws InvalidUserException {
        final User user = userFactory.produce(input.getUsername(), lang, password);
        AppLogger.fine(LOG, input::getRequestId, () -> "produced new User: " + user);
        return user;
    }

    private void trySavingNewUser(final RegistrationUseCase.Input input, final User newUser)
            throws UserAlreadyExistsException, GatewayException {
        AppLogger.fine(LOG, input::getRequestId, () -> "adding user to persistence layer...");
        inserter.insert(newUser);
        AppLogger.info(LOG, input::getRequestId, () -> "User \"" + newUser.getId() + "\" insert successfully");
    }
}
