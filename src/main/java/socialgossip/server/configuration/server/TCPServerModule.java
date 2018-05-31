package socialgossip.server.configuration.server;

import dagger.Module;
import dagger.Provides;
import socialgossip.server.entrypoints.tcp.TCPServer;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Module
public class TCPServerModule {
    final String serverName;
    final int    serverPort;

    public TCPServerModule(final String serverName, final int serverPort) {
        this.serverName = Objects.requireNonNull(serverName);
        this.serverPort = serverPort;
    }

    @Provides
    @ServerScope Integer provideTCPServerPort() {
        return serverPort;
    }

    @Provides
    @ServerScope TCPServer provideTCPServer(final ThreadPoolExecutor executor) {
        return new TCPServer(serverName, serverPort, executor);
    }
}
