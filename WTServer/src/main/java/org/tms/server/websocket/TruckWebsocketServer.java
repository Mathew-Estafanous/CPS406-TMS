package org.tms.server.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;
import org.tms.server.ITruckService;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.util.concurrent.ConcurrentHashMap;

public class TruckWebsocketServer {

    private final Server server;
    private final ServerConnector connector;

    public TruckWebsocketServer(int port, ITruckService truckService, ConcurrentHashMap<Integer, Session> sessionMap) {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);
        connector.setPort(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JavaxWebSocketServletContainerInitializer.configure(context, (servletContext, container) -> {
            container.setDefaultMaxTextMessageBufferSize(65535);
            container.addEndpoint(ServerEndpointConfig.Builder
                    .create(TruckServerController.class, "/truck/server")
                    .configurator(new TruckWebsocketConfigurator(truckService, sessionMap))
                    .build());
        });
    }

    public void start() throws Exception {
        System.out.println("Server started on port " + connector.getPort());
        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }
}
