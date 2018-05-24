package socialgossip.server.core.entities.auth;

import java.util.Date;
import java.util.Optional;

/**
 * Represents a permission inside the application.
 *
 * Every {@link Permission} has a token that identifies the scope of its
 * allowed actions.
 *
 * For example, a {@link socialgossip.server.core.entities.session.Session}
 * is a {@link Permission} to execute a number of actions through the
 * {@link socialgossip.server.core.entities.user.User} that owns it.
 */
public interface Permission {
    /**
     * Returns the token of the {@link Permission} that identifies abilities
     * and allowed actions for the permission itself.
     *
     * @return the permission token.
     */
    String getToken();

    Optional<Date> getExpireDate();

    boolean isExpired();
}
