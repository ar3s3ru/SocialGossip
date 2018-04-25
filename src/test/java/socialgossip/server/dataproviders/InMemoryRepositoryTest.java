package socialgossip.server.dataproviders;

import org.junit.Test;
import socialgossip.server.core.entities.password.Password;
import socialgossip.server.core.entities.session.Session;
import socialgossip.server.core.entities.user.User;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.core.gateways.user.UserNotFoundException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InMemoryRepositoryTest {
    @Test
    public void addNewUserSuccessfully() {
        try {
            final HashMap<String, User> injectedUserMap = new HashMap<>();
            final InMemoryRepository repository = new InMemoryRepository(injectedUserMap, null);
            final User user = new User("hello", Locale.getDefault(), mock(Password.class));
            repository.add(user);
            assertEquals(user, injectedUserMap.get(user.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void addNewUserTwiceFailing() {
        final HashMap<String, User> injectedUserMap = new HashMap<>();
        final InMemoryRepository repository = new InMemoryRepository(injectedUserMap, null);

        User user;
        try {
            user = new User("hello", Locale.getDefault(), mock(Password.class));
            repository.add(user);
            try {
                repository.add(user);
            } catch (UserAlreadyExistsException ok) {} // Do nothing, should pass
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void addAndRetrieveNewUser() {
        try {
            final InMemoryRepository repository = new InMemoryRepository();
            final User user = new User("hello", Locale.getDefault(), mock(Password.class));

            repository.add(user);
            assertEquals(user, repository.getByUsername(user.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void retrieveNotExistentUserFailing() {
        try {
            final InMemoryRepository repository = new InMemoryRepository();
            repository.getByUsername("hello");
        } catch (UserNotFoundException ok) {}
    }

    @Test
    public void addNewSessionSuccessfully() {
        try {
            final HashMap<String, Session> injectedSessionMap = new HashMap<>();
            final InMemoryRepository repository = new InMemoryRepository(null, injectedSessionMap);
            final Session session = new Session("helloworld", mock(User.class), InetAddress.getLocalHost());
            repository.add(session);
            assertEquals(session, injectedSessionMap.get(session.getToken()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
