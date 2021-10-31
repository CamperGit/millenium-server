package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.repos.teams.TeamInvitesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeamInvitesService {
    private TeamInvitesRepo teamInvitesRepo;

    @Autowired
    public void setTeamInvitesRepo(TeamInvitesRepo teamInvitesRepo) {
        this.teamInvitesRepo = teamInvitesRepo;
    }
}
