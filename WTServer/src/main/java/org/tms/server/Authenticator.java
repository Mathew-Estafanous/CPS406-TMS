package org.tms.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

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

    public Optional<String> toCredentials(String username, String password) {
        if (!isCorrectLogin(username, password)) {
            return Optional.empty();
        }
        final String token = JWT.create()
                .withIssuer("admin_auth")
                .withExpiresAt(Instant.now().plusSeconds(SECONDS_UNTIL_EXPIRE))
                .sign(algorithm);
        return Optional.of(token);
    }

    private boolean isCorrectLogin(String username, String password) {
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
}
