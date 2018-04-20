package socialgossip.server.core.entities.session;

import socialgossip.server.core.entities.auth.Permission;
import socialgossip.server.core.entities.user.User;

import java.net.InetAddress;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Login session that will authorize {@link User} to execute certain actions
 * inside the application.
 */
public class Session implements Permission {
    // Default expiration date is 1 day from now.
    // Can be overridden by a Session factory to produce new instances.
    private static final Supplier<Date> DEFAULT_EXPIRE_DATE_SUPPLIER =
            () -> Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

    /**
     * Checks if a proposed {@link Session} expiration {@link Date} is valid.
     * If the proposed {@link Date} is before now, a new expiration date set to 1 day from
     * now will be returned, instead.
     *
     * @param proposedExpireDate is the expiration date to validate.
     * @return the proposed expiration date if valid, or a new {@link Date} set for 1 day from
     *         now if not valid.
     */
    private static Date checkExpirationDate(final Date proposedExpireDate) {
        Objects.requireNonNull(proposedExpireDate);
        if (proposedExpireDate.toInstant().isBefore(Instant.now())) {
            return DEFAULT_EXPIRE_DATE_SUPPLIER.get();
        }
        return proposedExpireDate;
    }

    /**
     * Checks if a proposed {@link Session} token is valid or not.
     * A {@link Session} token can't be null, and must be at least 8 characters long.
     *
     * @param proposedToken is the token to validate.
     * @return the proposed token, if valid.
     * @throws InvalidTokenException if the token has been found invalid, explaining why.
     */
    private static String checkSessionToken(final String proposedToken)
            throws InvalidTokenException {
        Objects.requireNonNull(proposedToken);
        if (proposedToken.length() < 8) {
            throw new InvalidTokenException(proposedToken, "can't be less than 8 chars long");
        }
        return proposedToken;
    }

    private final String      token;
    private final User        user;
    private final Date        expireDate;
    private final InetAddress ipAddress;

    public Session(final String      token,
                   final User        user,
                   final Date        expireDate,
                   final InetAddress ipAddress) throws InvalidTokenException {
        this.token       = checkSessionToken(token);
        this.user        = Objects.requireNonNull(user);
        this.expireDate  = checkExpirationDate(expireDate);
        this.ipAddress   = Objects.requireNonNull(ipAddress);
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Checks if the {@link Session} is already expired.
     * @return true if the {@link Session} is expired, false otherwise.
     */
    public boolean isExpired() {
        return expireDate.toInstant().isBefore(Instant.now());
    }
}
