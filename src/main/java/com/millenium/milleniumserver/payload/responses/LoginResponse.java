package com.millenium.milleniumserver.payload.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Integer id;
    private String username;
    private Integer teamId;
    private String email;
    private final List<String> roles;
    private final String tokenType = "Bearer";

    public LoginResponse(String accessToken, String refreshToken, Integer id, String username, Integer teamId, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.teamId = teamId;
        this.email = email;
        this.roles = roles;
    }
}
