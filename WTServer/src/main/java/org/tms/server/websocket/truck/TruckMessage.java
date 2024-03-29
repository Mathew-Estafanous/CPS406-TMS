package org.tms.server.websocket.truck;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.tms.server.TruckState;

import javax.websocket.*;
import java.time.Duration;
import java.util.Objects;

public class TruckMessage {
    @SerializedName("type")
    private final TruckMessageType type;

    private final int truckID;
    private final String driverName;
    private final String estimatedTime;
    private final TruckState.LocationState locationState;
    private final int position;

    public TruckMessage(int truckID, TruckMessageType type, String driverName, String estimatedDockingTime, TruckState.LocationState locationState, int position) {
        this.truckID = truckID;
        this.type = type;
        this.driverName = driverName;
        this.estimatedTime = estimatedDockingTime;
        this.locationState = locationState;
        this.position = position;
    }

    public TruckMessage(TruckState state, TruckMessageType type) {
        this(state.getTruckDriver().getTruckID(),
            type,
            state.getTruckDriver().getDriverName(),
            state.getEstimatedTime().toString(),
            state.getLocationState(),
            state.getPosition());
    }

    public TruckMessage(int truckID, TruckMessageType type, String driverName, String estimatedDockingTime) {
        this(truckID, type, driverName, estimatedDockingTime, TruckState.LocationState.UNKNOWN, 0);
    }

    public TruckMessage(int truckID, TruckMessageType type, TruckState.LocationState locationState, int position) {
        this(truckID, type, "", Duration.ZERO.toString(), locationState, position);
    }

    public TruckMessage(int truckID, TruckMessageType stateUpdate, TruckState.LocationState waitingArea, int queuePosition, Duration waitTime) {
        this(truckID, stateUpdate, "", waitTime.toString(), waitingArea, queuePosition);
    }

    public int getTruckID() {
        return truckID;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public TruckMessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("TruckMessage{truckID=%d}", truckID);
    }

    public TruckState.LocationState getLocationState() {
        return locationState;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckMessage that = (TruckMessage) o;
        return truckID == that.truckID && position == that.position && type == that.type && Objects.equals(driverName, that.driverName) && Objects.equals(estimatedTime, that.estimatedTime) && locationState.equals(that.locationState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, truckID, driverName, estimatedTime, locationState, position);
    }

    public enum TruckMessageType {
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