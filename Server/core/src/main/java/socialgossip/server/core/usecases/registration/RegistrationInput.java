package socialgossip.server.core.usecases.registration;

public interface RegistrationInput {
    String getId();
    String getPassword();
    String getLang();
}