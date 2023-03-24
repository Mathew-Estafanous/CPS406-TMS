package org.tms.server.websocket.admin;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.tms.server.TruckDriver;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class AdminMessage {
    private final AdminMessageType type;

    private final int truckID;
    private final int newPosition;


    public AdminMessage(AdminMessageType type, int truckID, int newPosition) {
        this.type = type;
        this.truckID = truckID;
        this.newPosition = newPosition;
    }

    public AdminMessage(TruckDriver cancelledDriver, AdminMessageType cancel) {
        this(cancel, cancelledDriver.getTruckID(), 0);
    }

    public AdminMessageType getType() {
        return type;
    }

    public int getTruckID() {
        return truckID;
    }

    public int getNewPosition() {
        return newPosition;
    }


    public enum AdminMessageType {
        @SerializedName("cancel")
        CANCEL,
        @SerializedName("change_position")
        CHANGE_POSITION,
        @SerializedName("view_state")
        VIEW_STATE,
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
