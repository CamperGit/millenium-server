package com.millenium.milleniumserver.controller.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.payload.requests.categories.CategoryCreateRequest;
import com.millenium.milleniumserver.payload.requests.categories.CategoryDeleteRequest;
import com.millenium.milleniumserver.payload.requests.categories.CategoryEditRequest;
import com.millenium.milleniumserver.payload.responses.categories.CategoryDeleteResponse;
import com.millenium.milleniumserver.services.auth.TeamEntityService;
import com.millenium.milleniumserver.services.expenses.CategoriesService;
import com.millenium.milleniumserver.utils.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoriesService categoriesService;
    private TeamEntityService teamEntityService;
    private WebsocketUtils websocketUtils;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/createCategory")
    public void createNewCategory(@Payload @Valid CategoryCreateRequest request) {
        TeamEntity teamEntity = teamEntityService.getTeamEntityById(request.getTeamId());
        Category category = categoriesService.createNewCategory(request.getName(), teamEntity);
        websocketUtils.sendMessageToUsers(teamEntity.getUsers(), "/queue/categoriesUpdating", category);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/editCategory")
    public void editCategory(@Payload @Valid CategoryEditRequest request) {
        Category updatedCategory = categoriesService.updateCategory(request.getId(), request.getNewName());
        TeamEntity teamEntity = updatedCategory.getTeam();
        websocketUtils.sendMessageToUsers(teamEntity.getUsers(), "/queue/categoriesUpdating", updatedCategory);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/deleteCategory")
    public void deleteCategory(@Payload @Valid CategoryDeleteRequest request) {
        CategoryDeleteResponse response = categoriesService.deleteCategory(request.getId(), request.getDeleteExpenses());
        TeamEntity teamEntity = response.getDeletedCategory().getTeam();
        websocketUtils.sendMessageToUsers(teamEntity.getUsers(), "/queue/deletedCategories", response);
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
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }
}
