package com.millenium.milleniumserver.payload.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenRefreshResponse {
    private String accessToken;
    private Date accessTokenExpiryAt;
    private String refreshToken;
    private Date refreshTokenExpiryAt;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, Date accessTokenExpiryAt, String refreshToken, Date refreshTokenExpiryAt) {
        this.accessToken = accessToken;
        this.accessTokenExpiryAt = accessTokenExpiryAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryAt = refreshTokenExpiryAt;
    }
}

