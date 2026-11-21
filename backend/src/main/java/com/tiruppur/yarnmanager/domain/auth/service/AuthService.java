package com.tiruppur.yarnmanager.domain.auth.service;

import com.tiruppur.yarnmanager.domain.auth.dto.LoginRequest;
import com.tiruppur.yarnmanager.domain.auth.dto.LoginResponse;
import com.tiruppur.yarnmanager.domain.auth.entity.Role;
import com.tiruppur.yarnmanager.domain.auth.entity.User;
import com.tiruppur.yarnmanager.domain.auth.repository.UserRepository;
import com.tiruppur.yarnmanager.security.jwt.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse authenticate(LoginRequest request) {
        String username = request.getUsername().trim().toLowerCase();
        String password = request.getPassword().trim();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            String jwt = jwtUtils.generateJwtToken(authentication, user.getFullName());
            List<String> permissions = resolvePermissions(user.getRole());

            return LoginResponse.builder()
                    .token(jwt)
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .role(user.getRole().name().replace("ROLE_", ""))
                    .permissions(permissions)
                    .build();

        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid Employee ID or Password. (Demo: admin / admin123, supervisor / super123, qc_lead / qc123)");
        }
    }

    public LoginResponse getCurrentUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return LoginResponse.builder()
                .token(null)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole().name().replace("ROLE_", ""))
                .permissions(resolvePermissions(user.getRole()))
                .build();
    }

    private List<String> resolvePermissions(Role role) {
        return switch (role) {
            case ROLE_ADMIN -> List.of("READ_ALL", "WRITE_ALL", "APPROVE_DC", "DISPATCH", "MANAGE_USERS");
            case ROLE_YARN_MANAGER -> List.of("READ_YARN", "WRITE_YARN", "ISSUE_YARN", "READ_ORDERS");
            case ROLE_KNITTING_SUPERVISOR -> List.of("READ_STAGES", "WRITE_KNITTING", "LOG_SEWING", "READ_YARN");
            case ROLE_QC_INSPECTOR -> List.of("READ_ALL", "GRADE_ROLLS", "APPROVE_SHADES", "LOG_DHU");
            case ROLE_DISPATCH_OFFICER -> List.of("READ_PACKING", "CREATE_CARTON", "GENERATE_PACKING_LIST", "DISPATCH");
        };
    }
}
