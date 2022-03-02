package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.expenses.TeamLimit;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.teams.TeamInvite;
import com.millenium.milleniumserver.payload.requests.teams.TeamLimitEditRequest;
import com.millenium.milleniumserver.payload.responses.teams.UserPermissionsResponse;
import com.millenium.milleniumserver.service.teams.*;
import com.millenium.milleniumserver.util.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamEntityService teamEntityService;
    private TeamLimitsService teamLimitsService;
    private TeamInvitesService teamInvitesService;
    private PermissionEntityService permissionEntityService;
    private WebsocketUtils websocketUtils;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public TeamEntity createNewTeamWithUser(@RequestParam String name, @RequestParam Integer userId) {
        return teamEntityService.createNewTeam(name, userId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/applyJoin")
    public void applyTeamInvite(Integer inviteId) {
        teamInvitesService.applyTeamInvite(inviteId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/denyJoin")
    public void denyTeamInvite(Integer inviteId) {
        teamInvitesService.denyTeamInvite(inviteId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/categories")
    public List<Category> getCategoriesByTeam(@PathVariable("id") TeamEntity teamEntity) {
        return teamEntity.getCategories();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/invites")
    public List<TeamInvite> getTeamInvites(@PathVariable("id") TeamEntity teamEntity) {
        return teamInvitesService.getTeamInvites(teamEntity);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/permissions")
    public List<UserPermissionsResponse> getTeamUserPermissions(@PathVariable("id") TeamEntity teamEntity) {
        return permissionEntityService.getUserPermissionsInTeam(teamEntity);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/join")
    public boolean sendJoinRequest(@RequestParam String inviteLink, @RequestParam Integer userId) {
        return teamInvitesService.createTeamInvite(inviteLink, userId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/updateTeamLimit")
    public void updateTeamLimit(@Payload TeamLimitEditRequest request) {
        TeamLimit teamLimit = teamLimitsService.updateTeamLimit(request);
        TeamEntity team = teamLimit.getTeam();
        websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/updateTeamLimit", teamLimit);
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }

    @Autowired
    public void setTeamLimitsService(TeamLimitsService teamLimitsService) {
        this.teamLimitsService = teamLimitsService;
    }

    @Autowired
    public void setTeamInvitesService(TeamInvitesService teamInvitesService) {
        this.teamInvitesService = teamInvitesService;
    }

    @Autowired
    public void setPermissionEntityService(PermissionEntityService permissionEntityService) {
        this.permissionEntityService = permissionEntityService;
    }

    @Autowired
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }
}
