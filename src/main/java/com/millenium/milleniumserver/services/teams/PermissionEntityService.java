package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.teams.PermissionEntity;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.payload.responses.teams.UserPermissionsResponse;
import com.millenium.milleniumserver.repos.teams.PermissionEntityRepo;
import com.millenium.milleniumserver.services.auth.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PermissionEntityService {
    private PermissionEntityRepo permissionEntityRepo;
    private UserEntityService userEntityService;

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

    public PermissionEntity savePermission(PermissionEntity permissionEntity) {
        return permissionEntityRepo.save(permissionEntity);
    }

    public List<UserPermissionsResponse> getUserPermissionsInTeam(TeamEntity team) {
        List<UserPermissionsResponse> responses = new ArrayList<>();
        List<PermissionEntity> teamPermissions = permissionEntityRepo.findAllByTeamId(team.getTeamId());
        for (PermissionEntity permissionEntity : teamPermissions) {
            UserPermissionsResponse response = new UserPermissionsResponse();
            UserEntity user = userEntityService.findUserById(permissionEntity.getUserId());
            response.setPermissions(permissionEntity);
            response.setUsername(user.getUsername());
            responses.add(response);
        }
        return responses;
    }

    public List<PermissionEntity> updatePermissions(List<PermissionEntity> permissions) {
        return permissionEntityRepo.saveAll(permissions);
    }

    public PermissionEntity deletePermission(Integer userId, Integer teamId) {
        PermissionEntity permissionToDelete = permissionEntityRepo.findByTeamIdAndUserId(teamId, userId);
        if (permissionToDelete != null) {
            permissionEntityRepo.delete(permissionToDelete);
        }
        return permissionToDelete;
    }

    @Autowired
    public void setPermissionEntityRepo(PermissionEntityRepo permissionEntityRepo) {
        this.permissionEntityRepo = permissionEntityRepo;
    }

    @Autowired
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }
}
