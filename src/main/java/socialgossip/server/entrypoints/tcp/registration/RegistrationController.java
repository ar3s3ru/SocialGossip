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
import socialgossip.server.usecases.UseCase;
import socialgossip.server.usecases.registration.RegistrationUseCase;

import java.io.Writer;
import java.util.IllformedLocaleException;
import java.util.Optional;
import java.util.function.Consumer;

public class RegistrationController
        extends AbstractController<String, RegistrationUseCase.Input, Boolean> {

    public RegistrationController(final Presenter<String> presenter,
                                  final RegistrationUseCase interactor) {
        super(presenter, interactor);
    }

    @Override
    protected RegistrationUseCase.Input produceInputObject(final TCPRequest request, final JSONObject object) {
        final RegistrationJSONInput input = new RegistrationJSONInput(request.getId(), object);
        // Add to the context for later retrieval by output consumer
        Optional.ofNullable(input.getUsername()).ifPresent(
                u -> request.contextAdd("username", u)
        );
        return input;
    }

    @Override
    protected Consumer<Boolean> produceOutputConsumer(final TCPRequest request, final Writer responseWriter) {
        return (IOConsumer<Boolean>) (v) -> {
            responseWriter.write(presenter.getOkResponse(
                    (String) request.contextGet("username")
            ).toJSONString());
        };
    }

    @Override
    protected Consumer<Throwable> produceErrorHandler(final TCPRequest request, final Writer responseWriter) {
        return new RegistrationErrors() {
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
        };
    }
}
