package socialgossip.server.usecases.registration;

import java.util.Locale;

/**
 * Factory method for producing {@link Locale.Builder} instances.
 */
@FunctionalInterface
public interface LocaleBuilderFactory {
    /**
     * Returns an usable {@link Locale.Builder} instance.
     * @return an usable {@link Locale.Builder} instance.
     */
    Locale.Builder produce();
}
