package socialgossip.server.configuration.server;

import dagger.Subcomponent;
import socialgossip.server.configuration.presentation.PresentationComponent;
import socialgossip.server.configuration.presentation.PresentersModule;
import socialgossip.server.entrypoints.tcp.TCPServer;

@Subcomponent(modules = TCPServerModule.class)
@ServerScope
public interface ServerComponent {
    TCPServer tcpServer();
    Integer   tcpServerPort();

    PresentationComponent attachPresentationComponent(PresentersModule module);
}
