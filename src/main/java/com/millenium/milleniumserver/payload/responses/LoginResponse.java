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
    private List<Integer> teamsIds;
    private String email;
    private final List<String> roles;
    private final String tokenType = "Bearer";

    public LoginResponse(String accessToken, String refreshToken, Integer id, String username,  String email, List<String> roles, List<Integer> teamsIds) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.teamsIds = teamsIds;
        this.email = email;
        this.roles = roles;
    }
}
