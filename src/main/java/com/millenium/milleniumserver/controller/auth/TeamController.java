package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.payload.requests.TestNumberRequest;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamEntityService teamEntityService;

    @PostMapping
    public TeamEntity createNewTeam(@RequestParam String name, @RequestParam Integer number) {
        System.out.println(number);
        return teamEntityService.saveTeam(new TeamEntity(name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
    }

    @GetMapping
    public TeamEntity getTeamById(@RequestParam String name, @RequestParam Integer number) {
        System.out.println(name);
        System.out.println(number);
        return teamEntityService.getTeamEntityById(number);
    }

    @PostMapping(value = "/test")
    public void testRequestEntity(@RequestBody @Valid TestNumberRequest request) {
        System.out.println(request.getName());
        System.out.println(request.getNumber());
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }
}
