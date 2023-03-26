package org.tms.server;

import com.google.gson.Gson;
import org.tms.server.websocket.admin.AdminMessage;

/**
 * Credentials holds vital information of the Admin including their Username and SessionToken, and AdminMessageType.
 */
public record Credentials(AdminMessage.AdminMessageType type, String username, String sessionToken) {
    /**
     * A Collection of Admin Credentials.
     * @return a collection of Admin Credentials
     */
    public String toJson() {
        return new Gson().toJson(this);
    }
}
