package socialgossip.server.core.usecases.registration;

import org.junit.Assert;
import org.junit.Test;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.AddUserAccess;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.security.BcryptSchema;
import socialgossip.server.security.SimplePasswordValidator;

import java.util.IllformedLocaleException;
import java.util.Locale;

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
        final AddUserAccess userGateway = mock(AddUserAccess.class);
        new RegistrationInteractor(User::new, userGateway, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void usingNullLocaleBuilder() {
        final AddUserAccess userGateway = mock(AddUserAccess.class);
        final EncryptionSchema encryptionSchema = mock(EncryptionSchema.class);
        new RegistrationInteractor(User::new, userGateway, encryptionSchema, null);
    }

    @Test
    public void usingMockedConstructionArguments() {
        final UserFactory userFactory = mock(UserFactory.class);
        final AddUserAccess userAccess = mock(AddUserAccess.class);
        final EncryptionSchema encryptionSchema = mock(EncryptionSchema.class);
        final LocaleBuilderFactory localeBuilderFactory = mock(LocaleBuilderFactory.class);
        new RegistrationInteractor(userFactory, userAccess, encryptionSchema, localeBuilderFactory);
    }

    @Test
    public void runWithoutErrors() {
        final PasswordValidator passwordValidator = new SimplePasswordValidator();
        final EncryptionSchema<String> encryptionSchema = new BcryptSchema(passwordValidator);

        final AddUserAccess userAccess = mock(AddUserAccess.class);
        try {
            doReturn(true).when(userAccess).add(any(User.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final RegistrationInteractor useCase = new RegistrationInteractor(
                User::new, userAccess, encryptionSchema
        );

        useCase.execute(new RegistrationUseCase.Input() {
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
        }, Assert::assertTrue, new RegistrationErrors() {
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
    }
}
