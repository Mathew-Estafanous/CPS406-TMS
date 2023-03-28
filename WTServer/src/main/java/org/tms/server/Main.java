package org.tms.server;

import org.tms.server.websocket.WebsocketServer;

import javax.websocket.Session;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        final String dockingAreasValue = System.getenv("TOTAL_DOCKING_AREAS");
        int totalDockingAreas;
        try {
            totalDockingAreas = Integer.parseInt(dockingAreasValue);
            if (totalDockingAreas < 1) {
                throw new NumberFormatException("Must be greater than 1");
            }
        } catch (NumberFormatException | NullPointerException e) {
            throw new RuntimeException("Failed to get the total docking areas from TOTAL_DOCKING_AREAS: " + e.getMessage());
        }

        final Map<Integer, Session> sessionMap = new ConcurrentHashMap<>();
        WarehouseServer warehouseServer = new WarehouseServer(new TruckWaitingQueue(),
                new DockingAreaManager(totalDockingAreas),
                new NotificationService(sessionMap));

        final Authenticator authenticator;
        final String authKey = System.getenv("AUTH_KEY");
        validateEnvString(authKey, "AUTH_KEY");
        final String username = System.getenv("AUTH_USERNAME");
        validateEnvString(username, "AUTH_USERNAME");
        final String password = System.getenv("AUTH_PASSWORD");
        validateEnvString(password, "AUTH_PASSWORD");
        try {
            authenticator = new Authenticator(username, password, authKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create authenticator: " + e.getMessage());
        }

        int port;
        final String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isEmpty()) {
            port = Integer.parseInt(envPort);
        } else {
            port = 8080;
        }
        final WebsocketServer truckWsServer = new WebsocketServer(port, warehouseServer, sessionMap, warehouseServer, authenticator);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateEnvString(String value, String keyName) {
        if (value == null || value.isEmpty()) {
            throw new RuntimeException(String.format("Failed to get %s. Please set an environment variable", keyName));
        }
    }

}