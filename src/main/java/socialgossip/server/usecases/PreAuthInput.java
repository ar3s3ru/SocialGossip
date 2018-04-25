package socialgossip.server.usecases;

public interface PreAuthInput extends UseCase.Input {
    String getSessionToken();
}
