package socialgossip.server.usecases.lookup;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class LookupDetails {
    private final String username;
    private final Date   friendshipDate;

    public LookupDetails(final String username) {
        this(username, null);
    }

    public LookupDetails(final String username, final Date friendshipDate) {
        this.username       = Objects.requireNonNull(username);
        this.friendshipDate = friendshipDate;
    }

    public String getUsername() {
        return username;
    }

    public Optional<Date> getFriendshipDate() {
        return Optional.of(friendshipDate);
    }

    public boolean isFriend() {
        return getFriendshipDate().isPresent();
    }
}
