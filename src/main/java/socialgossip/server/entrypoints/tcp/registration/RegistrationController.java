package socialgossip.server.entrypoints.tcp.registration;

import org.json.simple.JSONObject;
import socialgossip.server.core.entities.password.InvalidPasswordException;
import socialgossip.server.core.entities.user.InvalidUserException;
import socialgossip.server.core.gateways.GatewayException;
import socialgossip.server.core.gateways.user.UserAlreadyExistsException;
import socialgossip.server.entrypoints.tcp.AbstractController;
import socialgossip.server.entrypoints.tcp.IOConsumer;
import socialgossip.server.entrypoints.tcp.TCPRequest;
import socialgossip.server.presentation.Presenter;
import socialgossip.server.usecases.registration.RegistrationErrors;
import socialgossip.server.usecases.registration.RegistrationUseCase;

import java.io.Writer;
import java.util.IllformedLocaleException;
import java.util.Objects;

public class RegistrationController
        extends AbstractController<RegistrationJSONInput, String> {

    private final RegistrationUseCase<Void, RegistrationErrors> interactor;

    public RegistrationController(final Presenter<String> presenter,
                                  final RegistrationUseCase<Void, RegistrationErrors> interactor) {
        super(presenter);
        this.interactor = Objects.requireNonNull(interactor);
    }

    @Override
    protected RegistrationJSONInput produceInputObject(final String requestId, final JSONObject object) {
        return new RegistrationJSONInput(requestId, object);
    }

    @Override
    protected void executor(final TCPRequest request, final RegistrationJSONInput input, final Writer responseWriter) {
        interactor.execute(
                input,
                (IOConsumer<Void>) (v) -> {
                    responseWriter.write(presenter.getOkResponse(input.getUsername()).toJSONString());
                },
                new RegistrationErrors() {
                    @Override
                    public void onInvalidLanguage(final IllformedLocaleException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onInvalidUser(final InvalidUserException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onUserAlreadyExists(final UserAlreadyExistsException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getFailResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onGatewayError(final GatewayException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getErrorResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onInvalidPassword(final InvalidPasswordException e) {
                        ((IOConsumer<Throwable>) (t) -> {
                            responseWriter.write(presenter.getFailResponse(t).toJSONString());
                        }).accept(e);
                    }

                    @Override
                    public void onError(Exception e) {
                        // TODO: finish this
                    }
                });
    }
}
