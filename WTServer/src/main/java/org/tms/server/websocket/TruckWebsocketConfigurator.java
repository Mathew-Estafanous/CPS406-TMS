package org.tms.server.websocket;

import org.tms.server.ITruckService;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.util.concurrent.ConcurrentHashMap;

public class TruckWebsocketConfigurator extends ServerEndpointConfig.Configurator {

    private final ITruckService truckService;
    private final ConcurrentHashMap<Integer, Session> sessionMap;

    public TruckWebsocketConfigurator(ITruckService truckService, ConcurrentHashMap<Integer, Session> sessionMap) {
        this.truckService = truckService;
        this.sessionMap = sessionMap;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (endpointClass == TruckServerController.class) {
            return (T) new TruckServerController(truckService, sessionMap);
        }
        return super.getEndpointInstance(endpointClass);
    }
}
