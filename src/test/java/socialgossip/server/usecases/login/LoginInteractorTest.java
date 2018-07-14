package socialgossip.server.usecases.login;

import org.junit.Test;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.password.PasswordValidator;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.notifications.Notifier;
import socialgossip.server.core.gateways.session.SessionAlreadyExistsException;
import socialgossip.server.core.gateways.session.SessionInserter;
import socialgossip.server.core.gateways.user.UserFinder;
import socialgossip.server.core.gateways.user.UserNotFoundException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class LoginInteractorTest {
    @Test
    public void runWithNotExistentUser() {
        final UserFinder userAccess = mock(UserFinder.class);
        try {
            doThrow(UserNotFoundException.class).when(userAccess).findByUsername("hello");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        final LoginInteractor interactor = new LoginInteractor(
                userAccess,
                mock(SessionInserter.class),
                mock(PasswordValidator.class),
                mock(SessionFactory.class),
                mock(Notifier.class)
        );

        interactor.execute(new LoginUseCase.Input() {
            @Override
            public String getUsername() { return "hello"; }

            @Override
            public String getPassword() { return "world"; }
            
            @Override
            public String getRequestId() { return "runWithNotExistentUserTest"; }
        }, (v) -> fail("should not complete!"), new LoginErrors() {
            @Override
            public void onUserNotFound(UserNotFoundException ok) { }

            @Override
            public void onSessionAlreadyExists(SessionAlreadyExistsException e) {
                fail(e.getMessage());
            }

            @Override
            public void onGatewayError(GatewayException e) { fail(e.getMessage()); }

            @Override
            public void onInvalidPassword(InvalidPasswordException e) { fail(e.getMessage()); }

            @Override
            public void onError(Exception e) { fail(e.getMessage()); }
        });
    }
}
