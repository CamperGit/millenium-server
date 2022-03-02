package com.millenium.milleniumserver.jwt;

import com.millenium.milleniumserver.entity.auth.RefreshTokenEntity;
import com.millenium.milleniumserver.service.auth.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class DeleteRefreshTokensTask {
    private RefreshTokenService refreshTokenService;

    @Scheduled(fixedRateString = "${jwt.refreshTokenTask.period}")
    public void deleteExpiredRefreshTokens() {
        log.info("Start deleting expired refresh tokens");
        int counter = 0;
        List<RefreshTokenEntity> refreshTokens = refreshTokenService.findAll();
        for (RefreshTokenEntity refreshToken : refreshTokens) {
            if (refreshToken.getExpiryTime().compareTo(Timestamp.from(Instant.now())) < 0) {
                refreshTokenService.delete(refreshToken);
                counter++;
            }
        }
        log.info("Completed the removal of expired refresh tokens. Deleted: " + counter);
    }

    @Autowired
    public void setRefreshTokenService(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }
}
