package com.millenium.milleniumserver.payload.responses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private Date accessTokenExpiryAt;
    private String refreshToken;
    private Date refreshTokenExpiryAt;
    private Integer id;
    private String username;
    private List<TeamEntity> teams;
    private String email;
    private final List<String> roles;
    private final String tokenType = "Bearer";

    public LoginResponse(String accessToken, Date accessTokenExpiryAt, String refreshToken, Date refreshTokenExpiryAt,
                         Integer id, String username, List<TeamEntity> teams, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.accessTokenExpiryAt = accessTokenExpiryAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryAt = refreshTokenExpiryAt;
        this.id = id;
        this.username = username;
        this.teams = teams;
        this.email = email;
        this.roles = roles;
    }
}
