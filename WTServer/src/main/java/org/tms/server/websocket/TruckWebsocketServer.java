package org.tms.server.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;

public class TruckWebsocketServer {

    private final Server server;
    private final ServerConnector connector;

    public TruckWebsocketServer(int port) {
        server = new Server();
        connector = new ServerConnector(server);
        server.addConnector(connector);
        connector.setPort(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        JavaxWebSocketServletContainerInitializer.configure(context, (servletContext, container) -> {
            container.setDefaultMaxTextMessageBufferSize(65535);
            container.addEndpoint(TruckServerController.class);
        });
    }

    public void start() throws Exception {
        System.out.println("Server started on port " + connector.getPort());
        server.start();
    }

    public void stop() throws Exception {
        System.out.println("Server is stopping");
        server.stop();
    }

    public void join() throws InterruptedException {
        server.join();
    }
}
