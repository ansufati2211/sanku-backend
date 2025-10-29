package com.sanku.sankuapibackend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    // --- CORRECCIÓN: ID estandarizado a Integer ---
    private Integer id;
    private String email;
    private List<String> roles;

    public AuthResponse(String accessToken, Integer id, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}