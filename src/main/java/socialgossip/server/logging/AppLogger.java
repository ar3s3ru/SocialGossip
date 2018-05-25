package socialgossip.server.logging;

import socialgossip.server.usecases.UseCase;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class for {@link Logger} to print formatted log messages throughout the Application.
 */
public final class AppLogger {
    public static void fine(final Logger logger,
                            final UseCase.Input input,
                            final Supplier<String> message) {
        log(Level.FINE, logger, input.getRequestId(), message);
    }

    public static void info(final Logger logger,
                            final UseCase.Input input,
                            final Supplier<String> message) {
        log(Level.INFO, logger, input.getRequestId(), message);
    }

    public static void warn(final Logger logger,
                            final UseCase.Input input,
                            final Supplier<String> message) {
        log(Level.WARNING, logger, input.getRequestId(), message);
    }

    public static void error(final Logger logger,
                             final UseCase.Input input,
                             final Supplier<String> message) {
        log(Level.SEVERE, logger, input.getRequestId(), message);
    }

    public static <E extends Exception> void exception(final Logger logger,
                                                       final UseCase.Input input,
                                                       final E exception) {
        error(logger, input, () -> exception.getClass().getName() + ": " + exception);
    }

    private static void log(final Level logLevel,
                            final Logger logger,
                            final String requestId,
                            final Supplier<String> message) {
        // Example: "FINE: [ 2017-01-01T00:00:01.12345Z | 000001 ]    Hello world!"
        Optional.ofNullable(logger)
                .ifPresent(logger1 -> logger1.log(logLevel, () ->
                        "[ "   + ISO8601Formatter.now() +
                        " | "  + requestId +
                        " ]\t" + message.get()
                ));
    }

    private static void log(final Level logLevel, final Logger logger,
                            final Supplier<String> requestId,
                            final Supplier<String> message) {
        Optional.ofNullable(logger).ifPresent(log -> {
            log.log(logLevel, () -> {
                final StringBuilder builder = new StringBuilder();
                builder.append("[ ");
                builder.append(ISO8601Formatter.now());
                Optional.ofNullable(requestId.get()).ifPresent(id -> {
                    builder.append(" | ");
                    builder.append(requestId);
                });
                builder.append(" ]\t");
                builder.append(Optional.ofNullable(message.get()).orElse("-- no message --"));
                return builder.toString();
            });
        });
    }
}
