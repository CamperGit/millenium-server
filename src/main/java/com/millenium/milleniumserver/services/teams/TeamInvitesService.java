package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.teams.PermissionEntity;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.teams.TeamInvite;
import com.millenium.milleniumserver.payload.responses.teams.UserPermissionsResponse;
import com.millenium.milleniumserver.repos.teams.TeamInvitesRepo;
import com.millenium.milleniumserver.services.auth.UserEntityService;
import com.millenium.milleniumserver.utils.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamInvitesService {
    private TeamInvitesRepo teamInvitesRepo;
    private TeamEntityService teamEntityService;
    private UserEntityService userEntityService;
    private PermissionEntityService permissionEntityService;
    private WebsocketUtils websocketUtils;

    public boolean createTeamInvite(String link, Integer userId) {
        TeamEntity team = teamEntityService.findTeamByInviteLink(link);
        UserEntity user = userEntityService.findUserById(userId);
        if (team != null && user != null) {
            if (!isCurrentInviteExist(team, user)) {
                TeamInvite joinRequest = teamInvitesRepo.save(new TeamInvite(team, user));
                websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/joinRequestUpdate", joinRequest);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void applyTeamInvite(Integer inviteId) {
        Optional<TeamInvite> teamInvite = teamInvitesRepo.findById(inviteId);
        teamInvite.ifPresent(invite -> {
            TeamEntity team = invite.getTeam();
            UserEntity user = invite.getUser();
            PermissionEntity permission = new PermissionEntity(team.getTeamId(), user.getUserId(), false, true, false, false, false);
            teamInvitesRepo.delete(invite);
            PermissionEntity savedPermission = permissionEntityService.savePermission(permission);
            websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/addNewUserToTeam", new UserPermissionsResponse(savedPermission, user.getUsername()));
        });
    }

    public void denyTeamInvite(Integer inviteId) {
        Optional<TeamInvite> teamInvite = teamInvitesRepo.findById(inviteId);
        teamInvite.ifPresent(invite -> teamInvitesRepo.delete(invite));
    }

    public boolean isCurrentInviteExist(TeamEntity team, UserEntity user) {
        return teamInvitesRepo.existsByTeamAndUser(team, user);
    }

    public List<TeamInvite> getTeamInvites(TeamEntity team) {
        return teamInvitesRepo.findAllByTeam(team);
    }

    @Autowired
    public void setTeamInvitesRepo(TeamInvitesRepo teamInvitesRepo) {
        this.teamInvitesRepo = teamInvitesRepo;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }

    @Autowired
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Autowired
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }

    @Autowired
    public void setPermissionEntityService(PermissionEntityService permissionEntityService) {
        this.permissionEntityService = permissionEntityService;
    }
}
