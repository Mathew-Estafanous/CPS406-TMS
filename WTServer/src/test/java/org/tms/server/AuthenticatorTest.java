package org.tms.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticatorTest {

    private static final String SIGNING_KEY = "signingKey";
    private final Algorithm algorithm = Algorithm.HMAC256(SIGNING_KEY);
    private Authenticator auth;
    private JWTVerifier verifier;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        verifier = JWT.require(algorithm)
                .withIssuer("admin_auth")
                .build();
        auth = new Authenticator("username", "password", SIGNING_KEY);
    }

    @Test
    void givenInvalidTokenFormat_whenAuthenticate_thenFalse() {
        assertFalse(auth.authenticate("invalid"));
    }

    @Test
    void givenExpiredToken_whenAuthenticate_thenFalse() {
        final String token = JWT.create()
                .withIssuer("admin_auth")
                .withExpiresAt(Instant.now().minusSeconds(1))
                .sign(algorithm);
        assertFalse(auth.authenticate(token));
    }

    @Test
    void givenValidToken_whenAuthenticate_thenTrue() {
        final String token = JWT.create()
                .withIssuer("admin_auth")
                .withExpiresAt(Instant.now().plusSeconds(3))
                .sign(algorithm);
        assertTrue(auth.authenticate(token));
    }

    @Test
    void givenIncorrectCredentials_whenToCredentials_thenEmpty() {
        assertTrue(auth.toCredentials("username", "incorrect").isEmpty());
    }

    @Test
    void givenCorrectCredentials_whenToCredentials_thenValidToken() {
        final String token = auth.toCredentials("username", "password").orElseThrow();
        assertDoesNotThrow(() -> verifier.verify(token));
        assertNotNull(verifier.verify(token));
    }
}