package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.entity.expenses.TeamLimit;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.payload.requests.teams.TeamLimitEditRequest;
import com.millenium.milleniumserver.repos.teams.TeamLimitsRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TeamLimitsService {
    private TeamLimitsRepo teamLimitsRepo;
    private TeamEntityService teamEntityService;

    @Transactional(readOnly = true)
    public TeamLimit getTeamLimitByMonthAndYear(Integer year, Integer month, TeamEntity team) {
        for (TeamLimit teamLimit : team.getLimits()) {
            if (teamLimit.getYear().equals(year) && teamLimit.getMonth().equals(month)) {
                return teamLimit;
            }
        }
        return null;
    }

    public TeamLimit updateTeamLimit(TeamLimitEditRequest request) {
        TeamEntity teamEntity = teamEntityService.findTeamById(request.getTeamId());
        Optional<TeamLimit> optional = teamEntity.getLimits().stream()
                .filter(l -> l.getMonth().equals(request.getMonth()))
                .filter(l -> l.getYear().equals(request.getYear()))
                .findFirst();
        TeamLimit teamLimit;
        if (optional.isPresent()) {
            teamLimit = optional.get();
            teamLimit.setLimit(request.getLimit());
        } else {
            teamLimit = new TeamLimit(request.getLimit(), request.getMonth(), request.getYear(), teamEntity);
        }
        TeamLimit savedTeamLimit = teamLimitsRepo.save(teamLimit);
        Hibernate.initialize(savedTeamLimit.getTeam());
        return savedTeamLimit;
    }

    @Autowired
    public void setTeamLimitsRepo(TeamLimitsRepo teamLimitsRepo) {
        this.teamLimitsRepo = teamLimitsRepo;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }
}
