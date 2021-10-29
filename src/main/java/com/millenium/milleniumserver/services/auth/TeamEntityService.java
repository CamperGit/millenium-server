package com.millenium.milleniumserver.services.auth;

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

@Service
@Transactional
public class TeamEntityService {
    private TeamEntityRepo teamEntityRepo;
    private CategoriesService categoriesService;

    @Transactional(readOnly = true)
    public TeamEntity getTeamEntityById(Integer teamId) {
        TeamEntity teamEntity = teamEntityRepo.findById(teamId).orElseThrow(EntityNotFoundException::new);
        Hibernate.initialize(teamEntity.getLimits());
        Hibernate.initialize(teamEntity.getCategories());
        return teamEntity;
    }

    public TeamEntity createNewTeam(String name, UserEntity user) {
        TeamEntity teamEntity = new TeamEntity(name, new ArrayList<>(), new ArrayList<>(), Collections.singletonList(user));
        TeamEntity savedTeam = teamEntityRepo.save(teamEntity);
        Category emptyCategory = categoriesService.createNewCategory("EMPTY", savedTeam);
        savedTeam.getCategories().add(emptyCategory);
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
}
