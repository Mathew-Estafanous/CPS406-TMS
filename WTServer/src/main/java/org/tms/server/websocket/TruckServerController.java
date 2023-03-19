package org.tms.server.websocket;

import org.tms.server.ITruckService;
import org.tms.server.TruckDriver;
import org.tms.server.TruckState;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

import static org.tms.server.websocket.TruckMessage.MessageType.*;

@ServerEndpoint(value = "/truck/server",
                decoders = TruckMessage.TruckMessageDecoder.class,
                encoders = TruckMessage.TruckMessageEncoder.class)
public class TruckServerController {

    private static final Logger log = Logger.getLogger(TruckServerController.class.getName());
    private final ITruckService truckService;

    public TruckServerController(ITruckService truckService) {
        this.truckService = truckService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        log.info("New connection opened");
    }

    @OnMessage
    public void onMessage(Session session, TruckMessage message) throws IOException {
        log.info("Message received: " + message);
        switch (message.getType()) {
            case CHECK_IN -> handleCheckIn(session, message);
            case CHECK_OUT -> handleCheckOut(message);
            case STATE_UPDATE -> handleStateUpdate(message);
        }
    }

    private void handleCheckIn(Session session, TruckMessage message) throws IOException {
        final Duration estDockingTime = Duration.parse(message.getEstimatedDockingTime());
        final TruckDriver driver = new TruckDriver(message.getTruckID(), message.getDriverName(), estDockingTime);
        final TruckState truckState = truckService.checkIn(driver);
        final TruckMessage response = new TruckMessage(truckState.getTruckID(), CHECK_IN, truckState.getInWaitingArea(), truckState.getPosition());
        try {
            session.getBasicRemote().sendObject(response);
        } catch (EncodeException e) {
            log.warning(String.format("Failed to encode check-in response object: %s", e.getMessage()));
        }
    }

    private void handleCheckOut(TruckMessage message) {
        truckService.checkOut(message.getTruckID());
    }

    private void handleStateUpdate(TruckMessage message) {
        truckService.getEntireTruckState(message.getTruckID());
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        log.info("Connection closed");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.warning("Error: " + t.getMessage());
    }
}
