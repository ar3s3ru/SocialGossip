package socialgossip.server.entrypoints.tcp;

import java.io.IOException;
import java.util.function.Consumer;

@FunctionalInterface
public interface IOConsumerWrapper<T> extends Consumer<T> {
    default void accept(final T elem) {
        try {
            acceptThrows(elem);
        } catch (IOException e) {
            
        }
    }

    void acceptThrows(T elem) throws IOException;
}
