package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.teams.PermissionEntity;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.payload.responses.teams.UserPermissionsResponse;
import com.millenium.milleniumserver.services.teams.PermissionEntityService;
import com.millenium.milleniumserver.services.teams.TeamEntityService;
import com.millenium.milleniumserver.utils.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private PermissionEntityService permissionEntityService;
    private TeamEntityService teamEntityService;
    private WebsocketUtils websocketUtils;

    @GetMapping
    public PermissionEntity getPermissionsByUserAndTeam(@RequestParam("teamId") Integer teamId, @RequestParam("userId") Integer userId) {
        return permissionEntityService.findPermissionByUserAndTeam(teamId, userId);
    }

    @PostMapping("/edit/{id}")
    public void editTeamPermissions(@RequestBody List<PermissionEntity> permissions, @PathVariable("id") TeamEntity team) {
        List<PermissionEntity> updatedPermissions = permissionEntityService.updatePermissions(permissions);
        List<UserPermissionsResponse> answer = new ArrayList<>();
        for (PermissionEntity updatedPermission : updatedPermissions) {
            for (UserEntity user : team.getUsers()) {
                if (updatedPermission.getUserId().equals(user.getUserId())) {
                    UserPermissionsResponse response = new UserPermissionsResponse();
                    response.setUsername(user.getUsername());
                    response.setPermissions(updatedPermission);
                    answer.add(response);
                }
            }
        }
        websocketUtils.sendMessageToUsers(team.getUsers(),"/queue/updateTeamPermissions", answer);
    }

    @DeleteMapping
    public void deleteUserFromTeam(@RequestParam Integer userId, @RequestParam Integer teamId) {
        TeamEntity team = teamEntityService.findTeamById(teamId);
        List<UserEntity> users = team.getUsers();
        PermissionEntity deletedPermission = permissionEntityService.deletePermission(userId, teamId);
        if (deletedPermission != null) {
            websocketUtils.sendMessageToUsers(users,"/queue/deletedPermissions", deletedPermission);
        }
    }

    @Autowired
    public void setPermissionEntityService(PermissionEntityService permissionEntityService) {
        this.permissionEntityService = permissionEntityService;
    }

    @Autowired
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }
}
