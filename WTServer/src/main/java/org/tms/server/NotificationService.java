package org.tms.server;

import org.tms.server.websocket.truck.TruckMessage;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

public class NotificationService {

    private static final Logger log = Logger.getLogger(NotificationService.class.getName());
    private final Map<Integer, Session> sessionMap;

    public NotificationService(Map<Integer, Session> sessionMap) {
        this.sessionMap = sessionMap;
    }

    /**
     * Sends a websocket message to the truck to notify it that it can start unloading.
     * @param driver The truck that can start unloading.
     * @param dockingNumber The docking number that the truck can use to start unloading.
     */
    public void notifyTruckStartedUnloading(TruckDriver driver, int dockingNumber) {
        final Session websocket = sessionMap.get(driver.getTruckID());
        if (websocket == null) return;
        final RemoteEndpoint.Basic session = websocket.getBasicRemote();
        final TruckMessage truckMessage = new TruckMessage(driver.getTruckID(),
                TruckMessage.TruckMessageType.STATE_UPDATE,
                TruckState.LocationState.DOCKING_AREA,
                dockingNumber, driver.getEstimatedDockingTime());
        try {
            session.sendObject(truckMessage);
        } catch (Exception e) {
            log.warning("Failed to send truck started unloading message: " + e.getMessage());
        }
    }

    /**
     * Send a websocket message to notify the truckID about an update in the waiting queue.
     * @param truckID The truckID that is waiting in the queue.
     * @param queuePosition The position of the truckID in the queue.
     * @param waitTime The estimated time the truckID has to wait.
     */
    public void notifyTruckUpdatedState(int truckID, int queuePosition, Duration waitTime) {
        final Session websocket = sessionMap.get(truckID);
        if (websocket == null) return;
        final RemoteEndpoint.Basic session = websocket.getBasicRemote();
        final TruckMessage truckMessage = new TruckMessage(truckID,
                TruckMessage.TruckMessageType.STATE_UPDATE,
                TruckState.LocationState.WAITING_AREA,
                queuePosition,
                waitTime);
        try {
            session.sendObject(truckMessage);
        } catch (Exception e) {
            log.warning("Failed to send truckID updated state message: " + e.getMessage());
        }
    }

    public void notifyTruckCancelled(int truckId) {
        final Session websocket = sessionMap.get(truckId);
        if (websocket == null) return;
        final RemoteEndpoint.Basic session = websocket.getBasicRemote();
        final TruckMessage truckMessage = new TruckMessage(truckId,
                TruckMessage.TruckMessageType.CHECK_OUT,
                TruckState.LocationState.LEAVING,
                -1);
        try {
            session.sendObject(truckMessage);
        } catch (Exception e) {
            log.warning("Failed to send truckID cancelled message: " + e.getMessage());
        }
    }
}
