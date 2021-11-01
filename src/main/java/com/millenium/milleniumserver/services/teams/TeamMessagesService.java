package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.entity.teams.TeamMessage;
import com.millenium.milleniumserver.repos.teams.TeamMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeamMessagesService {
    private TeamMessageRepo teamMessageRepo;

    public TeamMessage saveTeamMessage(TeamMessage teamMessage) {
        return teamMessageRepo.save(teamMessage);
    }

    @Autowired
    public void setTeamMessageRepo(TeamMessageRepo teamMessageRepo) {
        this.teamMessageRepo = teamMessageRepo;
    }
}
