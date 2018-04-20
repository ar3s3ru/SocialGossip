package socialgossip.server.core.usecases.logging;

import socialgossip.server.core.usecases.UseCase;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UseCaseLogger {
    public static void fine(final Logger logger,
                            final UseCase.Input input,
                            final Supplier<String> message) {
        log(Level.FINE, logger, input.getRequestId(), message);
    }

    private static void log(final Level logLevel,
                            final Logger logger,
                            final String requestId,
                            final Supplier<String> message) {
        Optional.ofNullable(logger)
                .ifPresent(logger1 -> {
                    logger1.log(logLevel, () -> requestId + ": " + message.get());
                });
    }
}
