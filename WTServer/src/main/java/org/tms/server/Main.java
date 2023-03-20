package org.tms.server;

import org.tms.server.websocket.TruckWebsocketServer;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        // TODO: Replace this with a real implementation when available.
        ITruckService mockService = new ITruckService() {
            @Override
            public TruckState checkIn(TruckDriver driver) {
                return new TruckState(driver, true, 3);
            }

            @Override
            public TruckState checkOut(int truckId) {
                return null;
            }

            @Override
            public TruckState getEntireTruckState(int truckId) {
                return null;
            }
        };
        final ConcurrentHashMap<Integer, Session> sessionMap = new ConcurrentHashMap<>();

        final TruckWebsocketServer truckWsServer = new TruckWebsocketServer(8080, mockService, sessionMap);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}