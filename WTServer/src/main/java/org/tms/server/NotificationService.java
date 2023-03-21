package org.tms.server;

import java.time.Duration;

public class NotificationService {
    public void notifyTruckStartedUnloading(TruckDriver nextTruck, int dockingNumber) {
        // TODO: Implement websocket message to notify the truck that it can start unloading.
    }

    public void notifyTruckUpdatedState(TruckDriver truck, int queuePosition, Duration waitTime) {
        // TODO: Implement websocket message to notify the truck that it's position in the queue has changed.
    }
}
