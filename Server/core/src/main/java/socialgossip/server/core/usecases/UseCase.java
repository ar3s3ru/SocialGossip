package socialgossip.server.core.usecases;

import java.util.Objects;

public class UseCase<I, O> {
    public interface Callback<T> {
        void onSuccess(T arg);
        void onError(Throwable error);
    }

    public void execute(I input, Callback<O> callback) {
        Objects.requireNonNull(input, "input can't be null");
        Objects.requireNonNull(callback, "callback can't be null");
    }
}
