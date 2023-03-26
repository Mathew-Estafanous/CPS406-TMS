package org.tms.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.tms.server.websocket.admin.AdminMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Authenticator represents a security system to authenticate Admin Credentials .
 */
public class Authenticator {


    private static final long SECONDS_UNTIL_EXPIRE = 1200;
    private static final Logger log = Logger.getLogger(Authenticator.class.getName());
    private final String adminUsername;
    private final String encryptedPassword;
    private final Algorithm algorithm;

    public Authenticator(String adminUsername, String adminPassword, String signingKey) throws NoSuchAlgorithmException, IllegalArgumentException {
        this.adminUsername = adminUsername;
        this.encryptedPassword = encrypt(adminPassword);
        this.algorithm = Algorithm.HMAC256(signingKey);
    }

    /**
     * Authenticates the Admins credentials.
     * @param credentials The credentials of the Admin.
     * @return Whether the credentials were valid or not.
     */
    public boolean authenticate(String credentials) {
        try {
            return JWT.require(algorithm)
                    .withIssuer("admin_auth")
                    .build().verify(credentials) != null;
        } catch (JWTVerificationException e) {
            log.warning("Authentication failed with: " + e.getMessage());
            return false;
        }
    }

    /**
     * Puts the Credentials into a collection.
     * @param username The username the Admin uses to access the Admin portal.
     * @param password The password the Admin uses to access the Admin portal.
     * @return The container that holds the credentials of all admins.
     */

    public Optional<Credentials> toCredentials(String username, String password) {
        if (!isCorrectLogin(username, password)) {
            return Optional.empty();
        }
        final String token = JWT.create()
                .withIssuer("admin_auth")
                .withExpiresAt(Instant.now().plusSeconds(SECONDS_UNTIL_EXPIRE))
                .sign(algorithm);
        return Optional.of(new Credentials(AdminMessage.AdminMessageType.LOGIN, username, token));
    }

    /**
     * Verifies the Login Information.
     * @param username The username the Admin uses to access the Admin portal.
     * @param password The password the Admin uses to access the Admin portal.
     * @return Whether the Login Information is correct.
     */
    private boolean isCorrectLogin(String username, String password) {
        try {
            return username.equals(adminUsername) && encryptedPassword.equals(encrypt(password));
        } catch (NoSuchAlgorithmException e) {
            log.warning("Failed to validate credentials: " + e.getMessage());
            return false;
        }
    }

    /**
     * Encrypts the Users password for security.
     * @param password The password the Admin uses to access the Admin portal.
     * @return A string holding the encrypted password.
     * @throws NoSuchAlgorithmException if the password cannot be encrypted
     */
    private String encrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
    }
}
