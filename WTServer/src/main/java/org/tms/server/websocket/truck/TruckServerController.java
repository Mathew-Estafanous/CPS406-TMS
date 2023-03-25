package org.tms.server.websocket.truck;

import org.tms.server.Cancellable;
import org.tms.server.ITruckService;
import org.tms.server.TruckDriver;
import org.tms.server.TruckState;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import static org.tms.server.websocket.truck.TruckMessage.TruckMessageType.*;

@ServerEndpoint(value = "/server/{truckID}",
                decoders = TruckMessage.TruckMessageDecoder.class,
                encoders = TruckMessage.TruckMessageEncoder.class)
public class TruckServerController<T extends ITruckService & Cancellable> {

    private static final Logger log = Logger.getLogger(TruckServerController.class.getName());
    private final T truckService;
    private final Map<Integer, Session> sessionMap;

    public TruckServerController(T truckService, Map<Integer, Session> sessionMap) {
        this.truckService = truckService;
        this.sessionMap = sessionMap;
    }

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("truckID") int truckID) {
        log.info("New connection opened with ID: " + truckID);
        session.setMaxIdleTimeout(0); // Remove maximum timeout.
        if(truckService.isTruckIDTaken(truckID)) return;
        sessionMap.put(truckID, session);
    }

    @OnMessage
    public void onMessage(Session session,
                          TruckMessage message) throws IOException {
        log.info("Message received: " + message);
        switch (message.getType()) {
            case CHECK_IN -> handleCheckIn(session, message);
            case CHECK_OUT -> handleCheckOut(session, message);
            case STATE_UPDATE -> handleStateUpdate(session, message);
        }
    }

    private void handleCheckIn(Session session, TruckMessage message) throws IOException {
        final Duration estDockingTime = Duration.parse(message.getEstimatedTime());
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
        scheduleCancelIfNotReconnected(closedSession.getKey());
    }

    private void scheduleCancelIfNotReconnected(int truckID) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (sessionMap.containsKey(truckID)) return;
                try {
                    truckService.cancelTruck(truckID);
                    log.info(String.format("Truck %d failed to open a new session and has been cancelled.", truckID));
                } catch (IllegalArgumentException e) {
                    log.info(String.format("Truck with ID %d is already out of the warehouse.", truckID));
                }
            }
        }, 10000);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.warning("Error: " + t.getMessage());
    }
}
