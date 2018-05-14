package socialgossip.server.usecases.friendship;

import java.util.Date;
import java.util.Objects;

public class FriendshipOutput {
    private final String friendUsername;
    private final Date   friendshipDate;

    public FriendshipOutput(String friendUsername, Date friendshipDate) {
        this.friendUsername = Objects.requireNonNull(friendUsername);
        this.friendshipDate = Objects.requireNonNull(friendshipDate);
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public Date getFriendshipDate() {
        return friendshipDate;
    }
}
