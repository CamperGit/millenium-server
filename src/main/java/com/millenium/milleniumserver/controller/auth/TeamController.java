package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.repos.teams.TeamInvitesRepo;
import com.millenium.milleniumserver.services.teams.TeamEntityService;
import com.millenium.milleniumserver.services.teams.TeamInvitesService;
import com.millenium.milleniumserver.services.teams.TeamLimitsService;
import com.millenium.milleniumserver.services.teams.TeamMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamEntityService teamEntityService;
    private TeamLimitsService teamLimitsService;
    private TeamMessagesService teamMessagesService;
    private TeamInvitesService teamInvitesService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public TeamEntity createNewTeamWithUser(@RequestParam String name, @RequestParam Integer userId) {
        return teamEntityService.createNewTeam(name, userId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/categories")
    public List<Category> getCategoriesByTeam(@PathVariable("id") TeamEntity teamEntity) {
        return teamEntity.getCategories();
    }

    @GetMapping
    public TeamEntity getTeamById(@RequestParam Integer teamId) {
        return teamEntityService.getTeamEntityById(teamId);
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
    public void setTeamMessagesService(TeamMessagesService teamMessagesService) {
        this.teamMessagesService = teamMessagesService;
    }

    @Autowired
    public void setTeamInvitesService(TeamInvitesService teamInvitesService) {
        this.teamInvitesService = teamInvitesService;
    }
}
