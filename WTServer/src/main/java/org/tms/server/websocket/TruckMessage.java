package org.tms.server.websocket;

import com.google.gson.Gson;

import javax.websocket.*;

public class TruckMessage {
    private final int truckID;
    private final String driverName;
    private final String estimatedDockingTime;

    public TruckMessage(int truckID, String driverName, String estimatedDockingTime) {
        this.truckID = truckID;
        this.driverName = driverName;
        this.estimatedDockingTime = estimatedDockingTime;
    }

    public String getEstimatedDockingTime() {
        return estimatedDockingTime;
    }

    public int getTruckID() {
        return truckID;
    }

    public String getDriverName() {
        return driverName;
    }

    @Override
    public String toString() {
        return String.format("TruckMessage{truckID=%d, driverName='%s', estimatedDockingTime=%s}", truckID, driverName, estimatedDockingTime);
    }

    public static class TruckMessageEncoder implements Encoder.Text<TruckMessage> {

        private static final Gson gson = new Gson();

        @Override
        public String encode(TruckMessage truckMessage) throws EncodeException {
            return gson.toJson(truckMessage);
        }

        @Override
        public void init(EndpointConfig endpointConfig) {}

        @Override
        public void destroy() {}
    }

    public static class TruckMessageDecoder implements Decoder.Text<TruckMessage> {

        private final Gson gson = new Gson();

        @Override
        public TruckMessage decode(String s) throws DecodeException {
            return gson.fromJson(s, TruckMessage.class);
        }

        @Override
        public boolean willDecode(String s) {
            return s != null;
        }

        @Override
        public void init(EndpointConfig config) {

        }

        @Override
        public void destroy() {

        }
    }
}