package org.tms.server.websocket.admin;

import com.google.gson.Gson;
import org.tms.server.*;
import org.tms.server.Authenticator;

import javax.websocket.*;
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
public class AdminPortal {

    private static final Logger log = Logger.getLogger(AdminPortal.class.getName());
    private final IAdminService adminService;
    private final Authenticator authenticator;

    public AdminPortal(IAdminService adminService, Authenticator authenticator) {
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
        if (message.getType() == LOGIN) {
            handleLoginCommand(session, message);
            return;
        }
        final String token = (String) session.getUserProperties().get("sessionToken");
        if (token == null || !authenticator.authenticate(token)) {
            AdminMessage response = new AdminMessage(FAILED, 0, 0);
            try {
                session.getBasicRemote().sendObject(response);
                return;
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

    @OnClose
    public void onClose(Session session) {
        log.info("Session Closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warning("Session Error: " + throwable.getMessage());
    }

    private void handleLoginCommand(Session session, AdminMessage message) {
        final String username = message.getUsername();
        final String password = message.getPassword();
        try {
            final Optional<Credentials> credentials = authenticator.toCredentials(username, password);
            if (credentials.isPresent()) {
                session.getBasicRemote().sendText(credentials.get().toJson());
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Open new connection to use login credentials"));
            } else {
                AdminMessage response = new AdminMessage(FAILED, 0, 0);
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
        final List<AdminMessage> adminResponse = allTruckStates.stream()
                .map(truckState -> new AdminMessage(truckState, AdminMessage.AdminMessageType.VIEW_STATE)).toList();
        try {
            session.getBasicRemote().sendText(new Gson().toJson(adminResponse));
        } catch (Exception e) {
            log.warning("Failed to send response to client: " + e.getMessage());
        }
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