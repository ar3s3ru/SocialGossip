package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.user.User;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public final class UserFriendship {
    private final User user;
    private final Date friendshipDate;

    public UserFriendship(final User user, final Date friendshipDate) {
        this.user           = Objects.requireNonNull(user);
        this.friendshipDate = friendshipDate;
    }

    public User getUser() {
        return user;
    }

    public Date getFriendshipDate() {
        return friendshipDate;
    }

    public boolean isFriend() {
        return Objects.nonNull(getFriendshipDate());
    }
}
