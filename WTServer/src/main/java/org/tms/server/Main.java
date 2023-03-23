package org.tms.server;

import org.tms.server.websocket.TruckWebsocketServer;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        final int totalDockingAreas = 5;
        final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();
        WarehouseServer warehouseServer = new WarehouseServer(new TruckWaitingQueue(),
                new DockingAreaManager(totalDockingAreas),
                new NotificationService(sessionMap));
        final TruckWebsocketServer truckWsServer = new TruckWebsocketServer(8080, warehouseServer, sessionMap);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}