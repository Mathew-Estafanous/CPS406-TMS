package org.tms.server.websocket;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.tms.server.TruckState;

import javax.websocket.*;
import java.util.Objects;

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

    public TruckMessage(TruckState state, MessageType type) {
        this(state.getTruckDriver().getTruckID(),
            type,
            state.getTruckDriver().getDriverName(),
            state.getTruckDriver().getEstimatedDockingTime().toString(),
            state.getInWaitingArea(),
            state.getPosition());
    }

    public TruckMessage(int truckID, MessageType type, String driverName, String estimatedDockingTime) {
        this(truckID, type, driverName, estimatedDockingTime, false, 0);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckMessage that = (TruckMessage) o;
        return truckID == that.truckID && position == that.position && type == that.type && Objects.equals(driverName, that.driverName) && Objects.equals(estimatedDockingTime, that.estimatedDockingTime) && inWaitingArea.equals(that.inWaitingArea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, truckID, driverName, estimatedDockingTime, inWaitingArea, position);
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