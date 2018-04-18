package socialgossip.server.core.usecases.login;

import java.util.Date;
import java.util.Objects;

public final class LoginOutput {
    private final String token;
    private final String user;
    private final Date   expiresAt;

    public LoginOutput(String token, String user, Date expiresAt) {
        this.token     = Objects.requireNonNull(token);
        this.user      = Objects.requireNonNull(user);
        this.expiresAt = Objects.requireNonNull(expiresAt);
    }

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }
}
