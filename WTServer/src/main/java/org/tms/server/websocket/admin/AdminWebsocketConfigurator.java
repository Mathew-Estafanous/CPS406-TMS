package org.tms.server.websocket.admin;

import org.tms.server.Authenticator;
import org.tms.server.IAdminService;

import javax.websocket.server.ServerEndpointConfig;

public class AdminWebsocketConfigurator extends ServerEndpointConfig.Configurator {
    private final IAdminService adminService;
    private final Authenticator authenticator;

    public AdminWebsocketConfigurator(IAdminService adminService, Authenticator authenticator) {
        this.adminService = adminService;
        this.authenticator = authenticator;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass == AdminPortal.class) {
            return (T) new AdminPortal(adminService, authenticator);
        }
        return super.getEndpointInstance(endpointClass);
    }
}
