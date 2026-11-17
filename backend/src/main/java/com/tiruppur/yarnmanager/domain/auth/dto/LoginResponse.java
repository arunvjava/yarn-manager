package com.tiruppur.yarnmanager.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String fullName;
    private String role; // ADMIN, YARN_MANAGER, KNITTING_SUPERVISOR, QC_INSPECTOR, DISPATCH_OFFICER
    private List<String> permissions;
}
