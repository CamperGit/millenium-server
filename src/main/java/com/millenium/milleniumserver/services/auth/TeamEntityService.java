package com.millenium.milleniumserver.services.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.repos.auth.TeamEntityRepo;
import com.millenium.milleniumserver.services.expenses.CategoriesService;
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
        return teamEntityRepo.findById(teamId).orElseThrow(EntityNotFoundException::new);
    }

    public TeamEntity createNewTeam(String name, UserEntity user) {
        TeamEntity teamEntity = new TeamEntity(name, new ArrayList<>(), new ArrayList<>(), Collections.singletonList(user));
        TeamEntity savedTeam = teamEntityRepo.save(teamEntity);
        categoriesService.createNewCategory("EMPTY", savedTeam);
        return getTeamEntityById(savedTeam.getTeamId());
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
