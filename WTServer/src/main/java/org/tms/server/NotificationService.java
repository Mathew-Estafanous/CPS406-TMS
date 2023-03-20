package org.tms.server;

import org.tms.server.websocket.TruckMessage;

public class NotificationService {
    /**
     * NotificationService represents a service used to send notifications to the truck driver.
     */

    public NotificationService() {
    }

    public TruckState notifyTruckUpdatedState(TruckDriver driver, TruckState state) {
        TruckMessage message = new TruckMessage(driver.getTruckID(), driver.getDriverName(), driver.getEstimatedDockingTime().toString());
        // TODO: interact with the websocket to send the message
        return state;
    }

    public void notifyTruckQueueCancelled(TruckDriver driver) {
        TruckMessage message = new TruckMessage(driver.getTruckID(), driver.getDriverName(), null);
        // TODO: interact with the websocket to send the message
    }

}
