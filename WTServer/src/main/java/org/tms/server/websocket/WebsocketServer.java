package org.tms.server.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.javax.server.config.JavaxWebSocketServletContainerInitializer;
import org.tms.server.Authenticator;
import org.tms.server.IAdminService;
import org.tms.server.ITruckService;
import org.tms.server.websocket.admin.AdminPortal;
import org.tms.server.websocket.admin.AdminWebsocketConfigurator;
import org.tms.server.websocket.truck.TruckServerController;
import org.tms.server.websocket.truck.TruckWebsocketConfigurator;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;

public class WebsocketServer {

    private final Server server;
    private final ServerConnector connector;

    public WebsocketServer(int port,
                           ITruckService truckService,
                           Map<Integer, Session> sessionMap,
                           IAdminService adminService,
                           Authenticator authenticator) {
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
                    .create(TruckServerController.class, "/server/{truckID}")
                    .configurator(new TruckWebsocketConfigurator(truckService, sessionMap))
                    .build());
            container.addEndpoint(ServerEndpointConfig.Builder
                    .create(AdminPortal.class, "/admin")
                    .configurator(new AdminWebsocketConfigurator(adminService, authenticator))
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
