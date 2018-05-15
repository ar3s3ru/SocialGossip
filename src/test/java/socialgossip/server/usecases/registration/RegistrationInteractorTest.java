package socialgossip.server.usecases.registration;

import org.junit.Assert;
import org.junit.Test;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.UserInserter;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.dataproviders.InMemoryRepository;
import socialgossip.server.security.BcryptSchema;
import socialgossip.server.security.SimplePasswordValidator;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RegistrationInteractorTest {
    @Test(expected = NullPointerException.class)
    public void usingNullUserFactory() {
        new RegistrationInteractor(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void usingNullUserGateway() {
        new RegistrationInteractor(User::new, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void usingNullEncryptionSchema() {
        final UserInserter userGateway = mock(UserInserter.class);
        new RegistrationInteractor(User::new, userGateway, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void usingNullLocaleBuilder() {
        final UserInserter userGateway = mock(UserInserter.class);
        final EncryptionSchema encryptionSchema = mock(EncryptionSchema.class);
        new RegistrationInteractor(User::new, userGateway, encryptionSchema, null);
    }

    @Test
    public void usingMockedConstructionArguments() {
        final UserFactory userFactory = mock(UserFactory.class);
        final UserInserter userAccess = mock(UserInserter.class);
        final EncryptionSchema encryptionSchema = mock(EncryptionSchema.class);
        final LocaleBuilderFactory localeBuilderFactory = mock(LocaleBuilderFactory.class);
        new RegistrationInteractor(userFactory, userAccess, encryptionSchema, localeBuilderFactory);
    }

    @Test
    public void runWithoutErrors() {
        final PasswordValidator passwordValidator = new SimplePasswordValidator();
        final EncryptionSchema<String> encryptionSchema = new BcryptSchema(passwordValidator);
        final InMemoryRepository userAccess = new InMemoryRepository();

        final RegistrationUseCase.Input input = new RegistrationUseCase.Input() {
            @Override
            public String getUsername() {
                return "hello";
            }

            @Override
            public String getPassword() {
                return "helloworld";
            }

            @Override
            public String getLanguage() {
                return Locale.getDefault().getISO3Language();
            }

            @Override
            public String getRequestId() {
                return "test";
            }
        };

        Logger.getLogger(RegistrationInteractor.class.getName()).setLevel(Level.FINE);
        new RegistrationInteractor(userAccess, encryptionSchema)
                .execute(input, Assert::assertTrue, new RegistrationErrors() {
            @Override
            public void onInvalidLanguage(IllformedLocaleException e) {
                fail(e.getMessage());
            }

            @Override
            public void onInvalidUser(InvalidUserException e) {
                fail(e.getMessage());
            }

            @Override
            public void onUserAlreadyExists(UserAlreadyExistsException e) {
                fail(e.getMessage());
            }

            @Override
            public void onGatewayError(GatewayException e) {
                fail(e.getMessage());
            }

            @Override
            public void onInvalidPassword(InvalidPasswordException e) {
                fail(e.getMessage());
            }

            @Override
            public void onError(Exception e) {
                fail(e.getMessage());
            }
        });

        try {
            final User user = userAccess.findByUsername(input.getUsername());
            assertEquals(input.getUsername(), user.getId());
            assertEquals(input.getLanguage(), user.getLang().getISO3Language());
            assertTrue(user.getPassword().verify(input.getPassword()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
