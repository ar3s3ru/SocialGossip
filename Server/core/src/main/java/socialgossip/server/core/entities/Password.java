package socialgossip.server.core.entities;

public interface Password {
    boolean isValid(String password);

    @FunctionalInterface
    interface Factory {
        Password produceNewPasswordFrom(String str) throws EmptyPasswordException;
    }
}