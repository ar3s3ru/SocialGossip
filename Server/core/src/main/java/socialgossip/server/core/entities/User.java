package socialgossip.server.core.entities;

import java.util.Locale;
import java.util.Objects;

public class User {
    private final String   id;
    private final Locale   lang;
    private final Password password;

    public User(String id, Locale lang, Password password) throws InvalidUserIdException {
        this.id       = checkIdAndReturn(id);
        this.lang     = Objects.requireNonNull(lang);
        this.password = Objects.requireNonNull(password);
    }

    private static String checkIdAndReturn(final String id) throws InvalidUserIdException {
        Objects.requireNonNull(id);
        if (id.length() == 0) {
            throw new InvalidUserIdException("user id is empty");
        }
        return id;
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

    @FunctionalInterface
    public interface Factory {
        User produceWith(String id, Locale lang, Password password) throws InvalidUserIdException;
    }
}
