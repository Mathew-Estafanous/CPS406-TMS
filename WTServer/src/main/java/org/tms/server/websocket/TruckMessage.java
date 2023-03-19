package org.tms.server.websocket;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import javax.websocket.*;

public class TruckMessage {
    @SerializedName("type")
    private final MessageType type;

    private final int truckID;
    private final String driverName;
    private final String estimatedDockingTime;

    private final Boolean inWaitingArea;
    private final int position;

    public TruckMessage(int truckID, MessageType type, String driverName, String estimatedDockingTime, Boolean inWaitingArea, int position) {
        this.truckID = truckID;
        this.type = type;
        this.driverName = driverName;
        this.estimatedDockingTime = estimatedDockingTime;
        this.inWaitingArea = inWaitingArea;
        this.position = position;
    }

    public TruckMessage(int truckID, MessageType type, Boolean inWaitingArea, int position) {
        this(truckID, type, "", "", inWaitingArea, position);
    }

    public int getTruckID() {
        return truckID;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getEstimatedDockingTime() {
        return estimatedDockingTime;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("TruckMessage{truckID=%d}", truckID);
    }

    public Boolean getInWaitingArea() {
        return inWaitingArea;
    }

    public int getPosition() {
        return position;
    }

    public enum MessageType {
        @SerializedName("check-in")
        CHECK_IN,
        @SerializedName("check-out")
        CHECK_OUT,
        @SerializedName("state-update")
        STATE_UPDATE
    }


    public static class TruckMessageEncoder implements Encoder.Text<TruckMessage> {

        private static final Gson gson = new Gson();

        @Override
        public String encode(TruckMessage truckMessage) {
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
        public TruckMessage decode(String s) {
            return gson.fromJson(s, TruckMessage.class);
        }

        @Override
        public boolean willDecode(String s) {
            return s != null;
        }

        @Override
        public void init(EndpointConfig config) {}

        @Override
        public void destroy() {}
    }
}