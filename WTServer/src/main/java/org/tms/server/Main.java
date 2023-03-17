package org.tms.server;

import org.tms.server.websocket.TruckWebsocketServer;

public class Main {
    public static void main(String[] args) {
        // TODO: Replace this with a real implementation when available.
        ITruckService mockService = new ITruckService() {
            @Override
            public TruckState checkIn(TruckDriver driver) {
                return null;
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

        final TruckWebsocketServer truckWsServer = new TruckWebsocketServer(8080, mockService);
        try {
            truckWsServer.start();
            truckWsServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}