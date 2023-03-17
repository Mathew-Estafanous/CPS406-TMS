package org.tms.server.websocket;

import org.tms.server.ITruckService;

import javax.websocket.server.ServerEndpointConfig;

public class TruckWebsocketConfigurator extends ServerEndpointConfig.Configurator {

    private final ITruckService truckService;

    public TruckWebsocketConfigurator(ITruckService truckService) {
        this.truckService = truckService;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass == TruckServerController.class) {
            return (T) new TruckServerController(truckService);
        }
        return super.getEndpointInstance(endpointClass);
    }
}
