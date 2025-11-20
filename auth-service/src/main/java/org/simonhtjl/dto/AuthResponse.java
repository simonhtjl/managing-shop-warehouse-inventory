package org.simonhtjl.dto;

import lombok.*;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private String fullName;

    public AuthResponse(String token, String username, String role, String fullName) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.fullName = fullName;
    }
}
