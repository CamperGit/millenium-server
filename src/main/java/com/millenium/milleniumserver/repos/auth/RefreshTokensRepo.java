package com.millenium.milleniumserver.repos.auth;

import com.millenium.milleniumserver.entity.auth.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokensRepo extends JpaRepository<RefreshTokenEntity, Long> {
}
