package org.tms.server.websocket.admin;

import org.tms.server.Authenticator;
import org.tms.server.IAdminService;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Arrays;
import java.util.List;

public class AdminWebsocketConfigurator extends ServerEndpointConfig.Configurator {
    private final IAdminService adminService;
    private final Authenticator authenticator;

    public AdminWebsocketConfigurator(IAdminService adminService, Authenticator authenticator) {
        this.adminService = adminService;
        this.authenticator = authenticator;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        final List<String> cookies = request.getHeaders().get("Cookie");
        if (cookies != null) {
            // iterate through cookies and split them up by ';' and collect as a List of Strings.
            cookies.replaceAll(val -> val.replaceAll("\\s", ""));
            cookies.stream()
                    .flatMap(val -> Arrays.stream(val.split(";")))
                    .map(val -> val.split("="))
                    .forEach(c -> sec.getUserProperties().put(c[0], (c.length > 1)? c[1]: ""));
        }
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass == AdminPortal.class) {
            return (T) new AdminPortal(adminService, authenticator);
        }
        return super.getEndpointInstance(endpointClass);
    }
}
