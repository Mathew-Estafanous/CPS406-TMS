package org.tms.server;

import org.tms.server.auth.Authenticator;
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
            final String authKey = System.getenv("WAREHOUSE_AUTH_KEY");
            if (authKey == null) {
                throw new RuntimeException("Failed to get WAREHOUSE_AUTH_KEY. Please set an environment variable");
            }
            authenticator = new Authenticator("admin", "password", authKey);
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