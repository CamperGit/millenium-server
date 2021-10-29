package com.millenium.milleniumserver.services.auth;

import com.millenium.milleniumserver.entity.auth.PermissionEntity;
import com.millenium.milleniumserver.entity.auth.PermissionEntityPK;
import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.repos.auth.TeamEntityRepo;
import com.millenium.milleniumserver.services.expenses.CategoriesService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

@Service
@Transactional
public class TeamEntityService {
    private TeamEntityRepo teamEntityRepo;
    private CategoriesService categoriesService;
    private PermissionEntityService permissionEntityService;

    @Transactional(readOnly = true)
    public TeamEntity getTeamEntityById(Integer teamId) {
        TeamEntity teamEntity = teamEntityRepo.findById(teamId).orElseThrow(EntityNotFoundException::new);
        Hibernate.initialize(teamEntity.getLimits());
        Hibernate.initialize(teamEntity.getCategories());
        return teamEntity;
    }

    public TeamEntity createNewTeam(String name, Integer userId) {
        TeamEntity team = new TeamEntity(name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        TeamEntity savedTeam = teamEntityRepo.save(team);

        Category emptyCategory = categoriesService.createNewCategory("EMPTY", team);
        team.getCategories().add(emptyCategory);

        permissionEntityService.setOwnerPermissionToUserInTeam(team.getTeamId(), userId);

        return savedTeam;
    }

    @Autowired
    public void setTeamEntityRepo(TeamEntityRepo teamEntityRepo) {
        this.teamEntityRepo = teamEntityRepo;
    }

    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Autowired
    public void setPermissionEntityService(PermissionEntityService permissionEntityService) {
        this.permissionEntityService = permissionEntityService;
    }
}
