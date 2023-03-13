package org.tms.server.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/truck/server")
public class TruckServerController {

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connection opened");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Message received: " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }
}
