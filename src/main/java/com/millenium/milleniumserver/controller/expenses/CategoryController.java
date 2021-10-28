package com.millenium.milleniumserver.controller.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.payload.requests.expenses.CategoryCreateRequest;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import com.millenium.milleniumserver.services.expenses.CategoriesService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoriesService categoriesService;
    private TeamEntityService teamEntityService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/createCategory")
    @SendTo("/topic/categories")
    public Category createNewCategory(CategoryCreateRequest request) {
        TeamEntity teamEntity = teamEntityService.getTeamEntityById(request.getTeamId());
        return categoriesService.createNewCategory(request.getName(), teamEntity);
    }

/*    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/createCategory")
    @SendTo("/topic/categories")
    public Category createNewCategory(@RequestParam String name, @RequestParam Integer teamId) {
        TeamEntity teamEntity = teamEntityService.getTeamEntityById(teamId);
        return categoriesService.createNewCategory(name, teamEntity);
    }*/

    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }
}
