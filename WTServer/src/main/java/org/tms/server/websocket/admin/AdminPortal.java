package org.tms.server.websocket.admin;

import org.tms.server.Cancellable;
import org.tms.server.IAdminService;
import org.tms.server.TruckDriver;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
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
            case VIEW_STATE -> viewServerStateCommand(session, message);
        }
    }

    private void viewServerStateCommand(Session session, AdminMessage message) {

    }

    private void changeQueuePositionCommand(Session session, AdminMessage message) {
        final boolean success = adminService.changeQueuePosition(message.getTruckID(), message.getNewPosition());
        final AdminMessage response = new AdminMessage(success ? AdminMessage.AdminMessageType.CHANGE_POSITION : AdminMessage.AdminMessageType.FAILED,
                message.getTruckID(), message.getNewPosition());
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