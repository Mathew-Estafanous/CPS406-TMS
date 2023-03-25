package org.tms.server.websocket.admin;

import org.tms.server.*;
import org.tms.server.auth.Authenticator;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.tms.server.websocket.admin.AdminMessage.AdminMessageType.FAILED;
import static org.tms.server.websocket.admin.AdminMessage.AdminMessageType.LOGIN;

@ServerEndpoint(value = "/admin",
                decoders = AdminMessage.AdminMessageDecoder.class,
                encoders = AdminMessage.AdminMessageEncoder.class)
public class AdminPortal<T extends IAdminService & Cancellable> {

    private static final Logger log = Logger.getLogger(AdminPortal.class.getName());
    private final T adminService;
    private final Authenticator authenticator;

    public AdminPortal(T adminService, Authenticator authenticator) {
        this.adminService = adminService;
        this.authenticator = authenticator;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("Session Opened: " + session.getId());
        session.setMaxIdleTimeout(0);
    }

    @OnMessage
    public void onMessage(Session session, AdminMessage message) {
        log.info("Message received: " + message);
        if (message.getType() == LOGIN) {
            handleLoginCommand(session, message);
            return;
        }
        final String token = (String) session.getUserProperties().get("token");
        if (token == null || !authenticator.authenticate(token)) {
            AdminMessage response = new AdminMessage(FAILED, 0, 0);
            try {
                session.getBasicRemote().sendObject(response);
            } catch (IOException | EncodeException e) {
                log.warning("Failed to send auth failed message: " + e.getMessage());
            }
        }

        switch (message.getType()) {
            case CANCEL -> cancelTruckCommand(session, message);
            case CHANGE_POSITION -> changeQueuePositionCommand(session, message);
            case VIEW_STATE -> viewServerStateCommand(session);
            default -> log.warning(String.format("Received unsupported message of type %s", message.getType().toString()));
        }
    }

    private void handleLoginCommand(Session session, AdminMessage message) {
        final String username = message.getUsername();
        final String password = message.getPassword();
        AdminMessage response;
        try {
            final Optional<String> token = authenticator.toCredentials(username, password);
            if (token.isPresent()) {
                session.getBasicRemote().sendText("Set-Cookie: token=" + token.get());
                response = new AdminMessage(LOGIN, 0 , 0);
                session.getBasicRemote().sendObject(response);
            } else {
                response = new AdminMessage(FAILED, 0, 0);
                session.getBasicRemote().sendObject(response);
            }
        } catch (IOException | EncodeException e) {
            log.warning("Failed to send login response: " + e.getMessage());
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