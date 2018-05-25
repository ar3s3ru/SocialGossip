package socialgossip.server.entrypoints.tcp;

import java.io.IOException;
import java.util.function.Consumer;

@FunctionalInterface
public interface IOConsumer<T> extends Consumer<T> {
    default void accept(final T elem) {
        try {
            acceptThrows(elem);
        } catch (IOException e) {
            // Log here or something
        }
    }

    void acceptThrows(T elem) throws IOException;
}
