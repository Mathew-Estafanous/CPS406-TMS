package org.tms.server.websocket.admin;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.tms.server.TruckDriver;
import org.tms.server.TruckState;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.time.Duration;
import java.util.Objects;

public class AdminMessage {
    private final AdminMessageType type;

    private final int truckID;
    private final int position;
    private final String driverName;
    private final String estimatedTime;
    private final TruckState.LocationState locationState;

    private final String username;
    private final String password;

    public AdminMessage(AdminMessageType type, int truckID, String driverName,
                        String estimatedTime, TruckState.LocationState locationState,
                        int position, String username, String password) {
        this.type = type;
        this.truckID = truckID;
        this.driverName = driverName;
        this.estimatedTime = estimatedTime;
        this.locationState = locationState;
        this.position = position;
        this.username = username;
        this.password = password;
    }

    public AdminMessage(AdminMessageType type, int truckID, int position) {
        this (type, truckID, "", Duration.ZERO.toString(), null, position, "", "");
    }

    public AdminMessage(AdminMessageType type, String username, String password) {
        this (type, 0, "", Duration.ZERO.toString(), null, 0, username, password);
    }

    public AdminMessage(TruckDriver driver, AdminMessageType type) {
        this(type, driver.getTruckID(), driver.getDriverName(), driver.getEstimatedDockingTime().toString(), TruckState.LocationState.UNKNOWN, 0, "", "");
    }

    public AdminMessage(TruckState state, AdminMessageType type) {
        this(state.getTruckDriver(), type, state.getPosition(), state.getLocationState(), state.getEstimatedTime().toString());
    }

    public AdminMessage(TruckDriver driver, AdminMessageType type, int position, TruckState.LocationState locationState, String estimatedTime) {
        this(type, driver.getTruckID(), driver.getDriverName(), estimatedTime, locationState, position, "", "");
    }

    public AdminMessageType getType() {
        return type;
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

    public TruckState.LocationState getLocationState() {
        return locationState;
    }

    public int getPosition() {
        return position;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminMessage that = (AdminMessage) o;
        return truckID == that.truckID && position == that.position && type == that.type && driverName.equals(that.driverName) && estimatedTime.equals(that.estimatedTime) && locationState == that.locationState && username.equals(that.username) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, truckID, position, driverName, estimatedTime, locationState, username, password);
    }

    public enum AdminMessageType {
        @SerializedName("cancel")
        CANCEL,
        @SerializedName("change_position")
        CHANGE_POSITION,
        @SerializedName("view_state")
        VIEW_STATE,
        @SerializedName("failed")
        FAILED,
        @SerializedName("login")
        LOGIN
    }

    public static class AdminMessageEncoder implements Encoder.Text<AdminMessage> {

        private static final Gson gson = new Gson();

        @Override
        public String encode(AdminMessage object) {
            return gson.toJson(object);
        }

        @Override
        public void init(EndpointConfig config) {}

        @Override
        public void destroy() {}
    }

    public static class AdminMessageDecoder implements Decoder.Text<AdminMessage> {

        private static final Gson gson = new Gson();

        @Override
        public AdminMessage decode(String s) {
            return gson.fromJson(s, AdminMessage.class);
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
