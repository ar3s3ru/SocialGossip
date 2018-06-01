package socialgossip.server.entrypoints.validation;

public interface Validable {
    void validate() throws ValidationException;
}
