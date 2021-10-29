package com.millenium.milleniumserver.controller.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.payload.requests.expenses.CategoryCreateRequest;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import com.millenium.milleniumserver.services.expenses.CategoriesService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoriesService categoriesService;
    private TeamEntityService teamEntityService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/createCategory")
    public void createNewCategory(@Payload CategoryCreateRequest request) {
        TeamEntity teamEntity = teamEntityService.getTeamEntityById(request.getTeamId());
        Category category = categoriesService.createNewCategory(request.getName(), teamEntity);
        List<UserEntity> users = teamEntity.getUsers();
        for (UserEntity userEntity : users) {
            simpMessagingTemplate.convertAndSendToUser(userEntity.getUsername(), "/queue/categories", category);
        }
    }

    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
