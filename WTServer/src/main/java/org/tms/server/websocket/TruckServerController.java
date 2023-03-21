package org.tms.server.websocket;

import org.tms.server.ITruckService;
import org.tms.server.TruckDriver;
import org.tms.server.TruckState;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import static org.tms.server.websocket.TruckMessage.MessageType.*;

@ServerEndpoint(value = "/server/{truckID}",
                decoders = TruckMessage.TruckMessageDecoder.class,
                encoders = TruckMessage.TruckMessageEncoder.class)
public class TruckServerController {

    private static final Logger log = Logger.getLogger(TruckServerController.class.getName());
    private final ITruckService truckService;
    private final Map<Integer, Session> sessionMap;

    public TruckServerController(ITruckService truckService, Map<Integer, Session> sessionMap) {
        this.truckService = truckService;
        this.sessionMap = sessionMap;
    }

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("truckID") int truckID) {
        log.info("New connection opened with ID: " + truckID);
        sessionMap.put(truckID, session);
    }

    @OnMessage
    public void onMessage(Session session, TruckMessage message) throws IOException {
        log.info("Message received: " + message);
        switch (message.getType()) {
            case CHECK_IN -> handleCheckIn(session, message);
            case CHECK_OUT -> handleCheckOut(session, message);
            case STATE_UPDATE -> handleStateUpdate(session, message);
        }
    }

    private void handleCheckIn(Session session, TruckMessage message) throws IOException {
        final Duration estDockingTime = Duration.parse(message.getEstimatedDockingTime());
        final TruckDriver driver = new TruckDriver(message.getTruckID(), message.getDriverName(), estDockingTime);
        final TruckState truckState = truckService.checkIn(driver);
        final TruckMessage response = new TruckMessage(truckState, CHECK_IN);
        try {
            session.getBasicRemote().sendObject(response);
        } catch (EncodeException e) {
            log.warning("Failed to encode check-in response object: " + e.getMessage());
        }
    }

    private void handleCheckOut(Session session, TruckMessage message) throws IOException {
        final TruckState state = truckService.checkOut(message.getTruckID());
        final TruckMessage response = new TruckMessage(state, CHECK_OUT);
        try {
            session.getBasicRemote().sendObject(response);
        } catch (EncodeException e) {
            log.warning("Failed to encode  check-in response object: " + e.getMessage());
        }
    }

    private void handleStateUpdate(Session session, TruckMessage message) throws IOException {
        final TruckState state = truckService.getCurrentTruckState(message.getTruckID());
        final TruckMessage response = new TruckMessage(state, STATE_UPDATE);
        try {
            session.getBasicRemote().sendObject(response);
        } catch (EncodeException e) {
            log.warning("Failed to encode check-in response object: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        final var sessionEntry = sessionMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(session))
                .findFirst();
        if (sessionEntry.isEmpty()) return;
        final Map.Entry<Integer, Session> closedSession = sessionEntry.get();
        sessionMap.remove(closedSession.getKey());
        log.info("Connection closed for session: " + closedSession.getKey());
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.warning("Error: " + t.getMessage());
    }
}
