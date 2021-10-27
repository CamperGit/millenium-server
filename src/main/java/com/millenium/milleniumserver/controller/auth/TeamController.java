package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import com.millenium.milleniumserver.services.auth.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamEntityService teamEntityService;
    private UserEntityService userEntityService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    public TeamEntity createNewTeamWithUser(@RequestParam String name, @RequestParam Integer userId) {
        UserEntity user = userEntityService.findUserById(userId);
        return teamEntityService.createNewTeam(name, user);
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
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }
}
