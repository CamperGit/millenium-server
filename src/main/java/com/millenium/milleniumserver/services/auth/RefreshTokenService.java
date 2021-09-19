package com.millenium.milleniumserver.services.auth;

import com.millenium.milleniumserver.entity.auth.RefreshTokenEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.exceptions.TokenException;
import com.millenium.milleniumserver.jwt.JwtUtils;
import com.millenium.milleniumserver.repos.auth.RefreshTokensRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RefreshTokenService {
    @Value("${jwt.refreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;
    private RefreshTokensRepo refreshTokensRepo;
    private UserEntityService userEntityService;
    private JwtUtils jwtUtils;


    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();

        refreshTokenEntity.setToken(jwtUtils.generateRefreshTokenFromUsername(userEntity.getUsername()));
        refreshTokenEntity.setExpiryTime(Timestamp.from(Instant.now().plusMillis(refreshTokenDurationMs)));
        refreshTokenEntity.setUser(userEntity);
        refreshTokenEntity.setUsed(false);

        return refreshTokensRepo.save(refreshTokenEntity);
    }

    public RefreshTokenEntity saveRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokensRepo.save(refreshTokenEntity);
    }

    @Transactional(readOnly = true)
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokensRepo.findByToken(token);
    }


    public void disableRefreshToken(RefreshTokenEntity token) {
        token.setUsed(true);
        token.setUser(null);
        refreshTokensRepo.save(token);
    }

    public void disableRefreshToken(UserEntity userEntity) {
        RefreshTokenEntity refreshTokenEntity = refreshTokensRepo.findByUser(userEntity);
        if (refreshTokenEntity != null) {
            disableRefreshToken(refreshTokenEntity);
        }
    }

    public RefreshTokenEntity checkUsage(RefreshTokenEntity token) {
        if (token.isUsed()) {
            throw new TokenException(token.getToken(), "The refresh token has already been used");
        }
        return token;
    }

    @Transactional(readOnly = true)
    public List<RefreshTokenEntity> findAll() {
        return refreshTokensRepo.findAll();
    }

    public void delete(RefreshTokenEntity refreshTokenEntity) {
        refreshTokensRepo.delete(refreshTokenEntity);
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Autowired
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Autowired
    public void setRefreshTokensRepo(RefreshTokensRepo refreshTokensRepo) {
        this.refreshTokensRepo = refreshTokensRepo;
    }
}
