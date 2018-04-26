package socialgossip.server.usecases.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Supplier;

public final class ISO8601Formatter {
    private static final DateFormat DATE_FORMAT = ((Supplier<DateFormat>) () -> {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(timeZone);
        return format;
    }).get();

    public static String from(final Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMAT.format(date);
    }

    public static String now() {
        return from(Date.from(Instant.now()));
    }
}
