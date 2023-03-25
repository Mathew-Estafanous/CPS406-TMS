package org.tms.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Authenticator {

    private static final Logger log = Logger.getLogger(Authenticator.class.getName());
    private final String adminUsername;
    private final String encryptedPassword;

    public Authenticator(String adminUsername, String adminPassword) throws NoSuchAlgorithmException {
        this.adminUsername = adminUsername;
        this.encryptedPassword = encrypt(adminPassword);
    }

    public boolean verifyCredentials(String username, String password) {
        try {
            return username.equals(adminUsername) && encryptedPassword.equals(encrypt(password));
        } catch (NoSuchAlgorithmException e) {
            log.warning("Failed to validate credentials: " + e.getMessage());
            return false;
        }
    }

    private String encrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
    }

    public void toCredentials(String username, String password) {

    }
}
