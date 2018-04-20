package socialgossip.server.core.usecases.registration;

import org.junit.Test;
import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.user.AddUserAccess;

import static org.mockito.Mockito.*;

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
}
