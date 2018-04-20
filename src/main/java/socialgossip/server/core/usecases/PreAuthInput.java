package socialgossip.server.core.usecases;

public interface PreAuthInput extends UseCase.Input {
    String getSessionToken();
}
