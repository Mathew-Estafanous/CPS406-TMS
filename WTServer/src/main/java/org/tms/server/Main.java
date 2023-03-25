package org.tms.server;

import org.tms.server.websocket.WebsocketServer;

import javax.websocket.Session;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        final int totalDockingAreas = 1;
        final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();
        WarehouseServer warehouseServer = new WarehouseServer(new TruckWaitingQueue(),
                new DockingAreaManager(totalDockingAreas),
                new NotificationService(sessionMap));
        final Authenticator authenticator;
        try {
            authenticator = new Authenticator("admin", "password");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create authenticator: " + e.getMessage());
        }
        final WebsocketServer truckWsServer = new WebsocketServer(8080, warehouseServer, sessionMap, warehouseServer, authenticator);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}