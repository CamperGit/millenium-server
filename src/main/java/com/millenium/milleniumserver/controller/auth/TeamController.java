package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import com.millenium.milleniumserver.services.auth.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamEntityService teamEntityService;
    private UserEntityService userEntityService;

    @PostMapping
    public TeamEntity createNewTeamWithUser(@RequestParam String name, @RequestParam Integer userId) {
        UserEntity user = userEntityService.findUserById(userId);
        return teamEntityService.saveTeam(new TeamEntity(name, new ArrayList<>(), new ArrayList<>(), Collections.singletonList(user)));
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
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }
}
