package org.tms.server;

import org.tms.server.websocket.TruckWebsocketServer;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        WarehouseServer warehouseServer = new WarehouseServer(new TruckWaitingQueue(), new DockingAreaManager(), new NotificationService());
        final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();
        final TruckWebsocketServer truckWsServer = new TruckWebsocketServer(8080, warehouseServer, sessionMap);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}