package com.millenium.milleniumserver.services.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.repos.auth.TeamEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class TeamEntityService {
    private TeamEntityRepo teamEntityRepo;

    public TeamEntity saveTeam(TeamEntity teamEntity) {
        return teamEntityRepo.save(teamEntity);
    }

    @Transactional(readOnly = true)
    public TeamEntity getTeamEntityById(Integer teamId) {
        return teamEntityRepo.findById(teamId).orElseThrow(EntityNotFoundException::new);
    }

    @Autowired
    public void setTeamEntityRepo(TeamEntityRepo teamEntityRepo) {
        this.teamEntityRepo = teamEntityRepo;
    }
}
