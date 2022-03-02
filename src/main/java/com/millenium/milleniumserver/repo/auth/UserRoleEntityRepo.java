package com.millenium.milleniumserver.repo.auth;

import com.millenium.milleniumserver.entity.auth.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleEntityRepo extends JpaRepository<UserRoleEntity, Integer> {
    Optional<UserRoleEntity> findByRole(String role);
}
