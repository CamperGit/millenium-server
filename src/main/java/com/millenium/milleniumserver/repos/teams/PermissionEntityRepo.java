package com.millenium.milleniumserver.repos.teams;

import com.millenium.milleniumserver.entity.teams.PermissionEntity;
import com.millenium.milleniumserver.entity.teams.PermissionEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionEntityRepo extends JpaRepository<PermissionEntity, PermissionEntityPK> {
    List<PermissionEntity> findByUserId(Integer userID);
    List<PermissionEntity> findByTeamId(Integer userID);
    PermissionEntity findByTeamIdAndUserId(Integer teamId, Integer userId);
}
