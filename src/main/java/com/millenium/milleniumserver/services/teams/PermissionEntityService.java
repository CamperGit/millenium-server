package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.entity.teams.PermissionEntity;
import com.millenium.milleniumserver.repos.teams.PermissionEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionEntityService {
    private PermissionEntityRepo permissionEntityRepo;

    public PermissionEntity findPermissionByUserAndTeam(Integer teamId, Integer userId) {
        return permissionEntityRepo.findByTeamIdAndUserId(teamId, userId);
    }

    public void setOwnerPermissionToUserInTeam(Integer teamId, Integer userId) {
        PermissionEntity permission = new PermissionEntity();
        permission.setUserId(userId);
        permission.setTeamId(teamId);
        permission.setAdding(true);
        permission.setReading(true);
        permission.setChanging(true);
        permission.setDeleting(true);
        permission.setModerating(true);
        permissionEntityRepo.save(permission);
    }

    @Autowired
    public void setPermissionEntityRepo(PermissionEntityRepo permissionEntityRepo) {
        this.permissionEntityRepo = permissionEntityRepo;
    }
}
