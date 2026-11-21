package com.tiruppur.yarnmanager.domain.auth;

import com.tiruppur.yarnmanager.domain.auth.dto.LoginRequest;
import com.tiruppur.yarnmanager.domain.auth.dto.LoginResponse;
import com.tiruppur.yarnmanager.domain.auth.entity.Role;
import com.tiruppur.yarnmanager.domain.auth.entity.User;
import com.tiruppur.yarnmanager.domain.auth.repository.UserRepository;
import com.tiruppur.yarnmanager.domain.auth.service.AuthService;
import com.tiruppur.yarnmanager.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authenticationManager, userRepository, jwtUtils);
    }

    @Test
    @DisplayName("Should successfully authenticate and return JWT with permissions for Admin")
    void shouldAuthenticateAdmin() {
        LoginRequest request = new LoginRequest("admin", "admin123");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        User mockUser = User.builder()
                .username("admin")
                .fullName("Ramasamy (Managing Director)")
                .role(Role.ROLE_ADMIN)
                .active(true)
                .build();

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(mockUser));
        when(jwtUtils.generateJwtToken(mockAuth, "Ramasamy (Managing Director)")).thenReturn("mock.jwt.token");

        LoginResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("admin", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        assertTrue(response.getPermissions().contains("READ_ALL"));
        assertTrue(response.getPermissions().contains("MANAGE_USERS"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when credentials are invalid")
    void shouldThrowOnInvalidCredentials() {
        LoginRequest request = new LoginRequest("wrong", "credentials");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(IllegalArgumentException.class, () -> authService.authenticate(request));
    }

    @Test
    @DisplayName("Should fetch authenticated user profile")
    void shouldFetchCurrentUserProfile() {
        User mockSupervisor = User.builder()
                .username("supervisor")
                .fullName("Muruganandam")
                .role(Role.ROLE_KNITTING_SUPERVISOR)
                .active(true)
                .build();

        when(userRepository.findByUsername("supervisor")).thenReturn(Optional.of(mockSupervisor));

        LoginResponse profile = authService.getCurrentUserProfile("supervisor");

        assertNotNull(profile);
        assertEquals("supervisor", profile.getUsername());
        assertEquals("KNITTING_SUPERVISOR", profile.getRole());
        assertTrue(profile.getPermissions().contains("WRITE_KNITTING"));
    }
}
