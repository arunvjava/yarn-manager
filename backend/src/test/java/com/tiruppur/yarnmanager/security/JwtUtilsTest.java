package com.tiruppur.yarnmanager.security;

import com.tiruppur.yarnmanager.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JWT Utilities Unit Tests")
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    // 256-bit secret key for testing
    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_MS = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", EXPIRATION_MS);
    }

    @Test
    @DisplayName("Should generate valid JWT token for authenticated principal")
    void shouldGenerateValidToken() {
        UserDetails userDetails = new User(
                "admin",
                "encodedPassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication, "Ramasamy (Managing Director)");

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("admin", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    @DisplayName("Should fail validation on tampered token")
    void shouldRejectTamperedToken() {
        UserDetails userDetails = new User(
                "supervisor",
                "encodedPassword",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_KNITTING_SUPERVISOR"))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication, "Muruganandam");
        String tamperedToken = token + "xyz";

        assertFalse(jwtUtils.validateJwtToken(tamperedToken));
    }

    @Test
    @DisplayName("Should reject malformed token strings")
    void shouldRejectMalformedToken() {
        assertFalse(jwtUtils.validateJwtToken("this.is.not.valid.jwt"));
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}
