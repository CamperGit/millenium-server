package com.millenium.milleniumserver.payload.responses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
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
    private List<TeamEntity> teams;
    private String email;
    private final List<String> roles;
    private final String tokenType = "Bearer";

    public LoginResponse(String accessToken, String refreshToken, Integer id, String username,  String email, List<String> roles, List<TeamEntity> teams) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.teams = teams;
        this.email = email;
        this.roles = roles;
    }
}
