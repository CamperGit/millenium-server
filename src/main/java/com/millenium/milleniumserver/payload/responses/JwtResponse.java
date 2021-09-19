package com.millenium.milleniumserver.payload.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;
    private Integer id;
    private String username;
    private final List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, Integer id, String username, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
