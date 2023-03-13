package org.tms.server;

import org.tms.server.websocket.TruckWebsocketServer;

public class Main {
    public static void main(String[] args) {
        final TruckWebsocketServer truckWsServer = new TruckWebsocketServer(8080);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}