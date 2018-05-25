package socialgossip.server.logging;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class for {@link Logger} to print formatted log messages throughout the Application.
 */
public final class AppLogger {
    public static void fine(final Logger logger,
                            final Supplier<String> requestId,
                            final Supplier<String> message) {
        log(Level.FINE, logger, requestId, message);
    }

    public static void info(final Logger logger,
                            final Supplier<String> requestId,
                            final Supplier<String> message) {
        log(Level.INFO, logger, requestId, message);
    }

    public static void warn(final Logger logger,
                            final Supplier<String> requestId,
                            final Supplier<String> message) {
        log(Level.WARNING, logger, requestId, message);
    }

    public static void error(final Logger logger,
                             final Supplier<String> requestId,
                             final Supplier<String> message) {
        log(Level.SEVERE, logger, requestId, message);
    }

    public static <E extends Exception> void exception(final Logger logger,
                                                       final Supplier<String> requestId,
                                                       final E exception) {
        error(logger, requestId, () -> exception.getClass().getName() + ": " + exception);
    }

    private static void log(final Level logLevel, final Logger logger,
                            final Supplier<String> requestId,
                            final Supplier<String> message) {
        Optional.ofNullable(logger).ifPresent(log -> {
            log.log(logLevel, () -> {
                final StringBuilder builder = new StringBuilder();
                builder.append("[ ");
                builder.append(ISO8601Formatter.now());
                Optional.ofNullable(requestId).ifPresent(supplier ->
                        Optional.ofNullable(supplier.get()).ifPresent(id -> {
                            builder.append(" | ");
                            builder.append(id);
                        })
                );
                builder.append(" ]\t");
                builder.append(Optional.ofNullable(message).map(supplier ->
                        Optional.ofNullable(message.get()).orElse("-- no message --")
                ).orElse("-- no message supplier --"));
                return builder.toString();
            });
        });
    }
}
