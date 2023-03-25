package org.tms.server.websocket.admin;

import org.tms.server.*;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ServerEndpoint(value = "/admin",
                decoders = AdminMessage.AdminMessageDecoder.class,
                encoders = AdminMessage.AdminMessageEncoder.class)
public class AdminPortal<T extends IAdminService & Cancellable> {

    private static final Logger log = Logger.getLogger(AdminPortal.class.getName());
    private final T adminService;

    public AdminPortal(T adminService) {
        this.adminService = adminService;
    }

    @OnMessage
    public void onMessage(Session session, AdminMessage message) {
        log.info("Message received: " + message);
        switch (message.getType()) {
            case CANCEL -> cancelTruckCommand(session, message);
            case CHANGE_POSITION -> changeQueuePositionCommand(session, message);
            case VIEW_STATE -> viewServerStateCommand(session);
        }
    }

    private void viewServerStateCommand(Session session) {
        final WarehouseState warehouseState = adminService.viewEntireWarehouseState();
        // combine the two lists for waiting truck drivers and docking truck drivers
        final List<TruckState> allTruckStates = new ArrayList<>(warehouseState.waitingTruckDrivers());
        allTruckStates.addAll(warehouseState.dockingTruckDrivers());

        allTruckStates.forEach(truckState -> {
            final AdminMessage response = new AdminMessage(truckState.getTruckDriver(),
                    AdminMessage.AdminMessageType.VIEW_STATE,
                    truckState.getPosition(),
                    truckState.getLocationState(),
                    truckState.getEstimatedTime().toString());
            try {
                session.getBasicRemote().sendObject(response);
            } catch (Exception e) {
                log.warning("Failed to send response to client: " + e.getMessage());
            }
        });
    }

    private void changeQueuePositionCommand(Session session, AdminMessage message) {
        final boolean success = adminService.changeQueuePosition(message.getTruckID(), message.getPosition());
        final AdminMessage response = new AdminMessage(success ? AdminMessage.AdminMessageType.CHANGE_POSITION : AdminMessage.AdminMessageType.FAILED, message.getTruckID(), message.getPosition());
        try {
            session.getBasicRemote().sendObject(response);
        } catch (Exception e) {
            log.warning("Failed to send chane queue response: " + e.getMessage());
        }
    }

    private void cancelTruckCommand(Session session, AdminMessage message) {
        final TruckDriver cancelledDriver = adminService.cancelTruck(message.getTruckID());
        final AdminMessage response = new AdminMessage(cancelledDriver, AdminMessage.AdminMessageType.CANCEL);
        try {
            session.getBasicRemote().sendObject(response);
        } catch (Exception e) {
            log.warning("Failed to send response to client: " + e.getMessage());
        }
    }
}