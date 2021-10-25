package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamEntityService teamEntityService;

    @PostMapping
    public TeamEntity createNewTeam(@RequestParam String name) {
        return teamEntityService.saveTeam(new TeamEntity(name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    @GetMapping
    public TeamEntity getTeamById(@RequestParam Integer teamId) {
        return teamEntityService.getTeamEntityById(teamId);
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }
}
