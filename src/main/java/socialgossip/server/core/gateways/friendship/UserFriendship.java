package socialgossip.server.core.gateways.friendship;

import socialgossip.server.core.entities.friendship.Friendship;
import socialgossip.server.core.entities.user.User;

import java.util.Objects;
import java.util.Optional;

public final class UserFriendship {
    private final User user;
    private final Friendship friendship;

    public UserFriendship(final User user, final Friendship friendship) {
        this.user       = Objects.requireNonNull(user);
        this.friendship = friendship;
    }

    public User getUser() {
        return user;
    }

    public Optional<Friendship> onFriendship() {
        return Optional.ofNullable(friendship);
    }
}
