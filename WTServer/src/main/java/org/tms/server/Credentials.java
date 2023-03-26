package org.tms.server;

import com.google.gson.Gson;
import org.tms.server.websocket.admin.AdminMessage;

public record Credentials(AdminMessage.AdminMessageType type, String username, String sessionToken) {
    /**
     * Cancels a truck that is either in the waiting area or docking area.
     * @return a collection of Admin Credentials
     */
    public String toJson() {
        return new Gson().toJson(this);
    }
}
