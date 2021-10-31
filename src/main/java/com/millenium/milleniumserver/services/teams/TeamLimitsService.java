package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.repos.teams.TeamLimitsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeamLimitsService {
    private TeamLimitsRepo teamLimitsRepo;

    @Autowired
    public void setTeamLimitsRepo(TeamLimitsRepo teamLimitsRepo) {
        this.teamLimitsRepo = teamLimitsRepo;
    }
}
