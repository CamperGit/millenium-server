package com.millenium.milleniumserver.repo.auth;

import com.millenium.milleniumserver.entity.auth.RefreshTokenEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokensRepo extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity findByUser(UserEntity user);
}
