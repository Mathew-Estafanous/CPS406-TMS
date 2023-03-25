package org.tms.server;

import com.google.gson.Gson;
import org.tms.server.websocket.admin.AdminMessage;

public record Credentials(AdminMessage.AdminMessageType type, String username, String sessionToken) {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
