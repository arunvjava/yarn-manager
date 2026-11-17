package com.tiruppur.yarnmanager.domain.auth.service;

import com.tiruppur.yarnmanager.domain.auth.dto.LoginRequest;
import com.tiruppur.yarnmanager.domain.auth.dto.LoginResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    public LoginResponse authenticate(LoginRequest request) {
        String username = request.getUsername().trim().toLowerCase();
        String password = request.getPassword().trim();

        // Standard Tiruppur Factory Profiles
        if ("admin".equals(username) && "admin123".equals(password)) {
            return LoginResponse.builder()
                    .token("jwt-session-" + UUID.randomUUID())
                    .username("admin")
                    .fullName("Ramasamy (Managing Director)")
                    .role("ADMIN")
                    .permissions(List.of("READ_ALL", "WRITE_ALL", "APPROVE_DC", "DISPATCH"))
                    .build();
        } else if ("supervisor".equals(username) && "super123".equals(password)) {
            return LoginResponse.builder()
                    .token("jwt-session-" + UUID.randomUUID())
                    .username("supervisor")
                    .fullName("Muruganandam (Knitting & Sewing Head)")
                    .role("KNITTING_SUPERVISOR")
                    .permissions(List.of("READ_STAGES", "WRITE_KNITTING", "LOG_SEWING"))
                    .build();
        } else if ("qc_lead".equals(username) && "qc123".equals(password)) {
            return LoginResponse.builder()
                    .token("jwt-session-" + UUID.randomUUID())
                    .username("qc_lead")
                    .fullName("Senthil Kumar (Quality Head)")
                    .role("QC_INSPECTOR")
                    .permissions(List.of("READ_ALL", "GRADE_ROLLS", "APPROVE_SHADES"))
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid Employee ID or Password. (Demo: admin / admin123, supervisor / super123, qc_lead / qc123)");
        }
    }
}
