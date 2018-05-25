package socialgossip.server;

import socialgossip.server.core.entities.password.EncryptionSchema;
import socialgossip.server.dataproviders.InMemoryRepository;
import socialgossip.server.entrypoints.tcp.TCPServer;
import socialgossip.server.entrypoints.tcp.registration.RegistrationController;
import socialgossip.server.logging.AppLogger;
import socialgossip.server.presentation.registration.RegistrationPresenter;
import socialgossip.server.security.BcryptSchema;
import socialgossip.server.security.SimplePasswordValidator;
import socialgossip.server.usecases.registration.RegistrationInteractor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
    public static void main(String[] args) {
        final ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final TCPServer server = new TCPServer("tcp", 8080, executor);

        final BcryptSchema bcryptSchema = new BcryptSchema(new SimplePasswordValidator());

        final InMemoryRepository repository = new InMemoryRepository();
        final RegistrationInteractor interactor = new RegistrationInteractor(repository, bcryptSchema);

        final RegistrationPresenter presenter = new RegistrationPresenter();
        final RegistrationController controller = new RegistrationController(server, interactor, presenter);

        Logger.getLogger(RegistrationInteractor.class.getName()).setLevel(Level.ALL);

        AppLogger.info(Logger.getGlobal(), null, () -> "starting TCP server...");
        final Future future = executor.submit(server);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {

        }
    }
}
