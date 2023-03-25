package org.tms.server.websocket.admin;

import org.tms.server.Authenticator;
import org.tms.server.IAdminService;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Optional;

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
            final Optional<String[]> authToken = cookies.stream().map(val -> val.split("=", 2))
                    .filter(c -> c[0].equals("token"))
                    .findFirst();
            authToken.ifPresent(val -> sec.getUserProperties().put("token", (val.length > 1)? val[1]: ""));
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
