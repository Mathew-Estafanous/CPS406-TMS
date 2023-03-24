package org.tms.server.websocket.admin;

import org.tms.server.IAdminService;

import javax.websocket.server.ServerEndpointConfig;

public class AdminWebsocketConfigurator extends ServerEndpointConfig.Configurator {
    private final IAdminService adminService;

    public AdminWebsocketConfigurator(IAdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass == AdminPortal.class) {
            return (T) new AdminPortal(adminService);
        }
        return super.getEndpointInstance(endpointClass);
    }
}
