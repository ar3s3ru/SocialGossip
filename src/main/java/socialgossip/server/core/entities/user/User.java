package socialgossip.server.core.entities.user;

import socialgossip.server.core.entities.password.Password;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents an user of the application.
 */
public class User {
    private final String   id;
    private final Locale   lang;
    private final Password password;

    public User(String id, Locale lang, Password password) throws InvalidUserException {
        this.id       = checkIdAndReturn(id);
        this.lang     = Objects.requireNonNull(lang);
        this.password = Objects.requireNonNull(password);
    }

    /**
     * Checks if the {@link User} id is valid, returning the id in the positive case.
     * Raises an {@link InvalidUserException}, otherwise.
     * @param id is the id to check.
     * @return the valid id.
     * @throws InvalidUserException if the id is not valid.
     */
    private static String checkIdAndReturn(final String id) throws InvalidUserException {
        final String notNullId = Objects.requireNonNull(id);
        if (notNullId.length() == 0) {
            throw new InvalidUserException("user id is empty");
        }
        return notNullId;
    }

    public String getId() {
        return id;
    }

    public Locale getLang() {
        return lang;
    }

    public Password getPassword() {
        return password;
    }
}
