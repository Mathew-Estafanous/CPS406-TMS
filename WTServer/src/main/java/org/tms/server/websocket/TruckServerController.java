package org.tms.server.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint(value = "/truck/server",
                decoders = TruckMessage.TruckMessageDecoder.class,
                encoders = TruckMessage.TruckMessageEncoder.class)
public class TruckServerController {

    private static final Logger log = Logger.getLogger(TruckServerController.class.getName());

    @OnOpen
    public void onOpen(Session session) throws IOException {
        log.info("New connection opened");
    }

    @OnMessage
    public void onMessage(Session session, TruckMessage message) throws IOException {
        log.info("Message received: " + message);
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
