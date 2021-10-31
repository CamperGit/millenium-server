package com.millenium.milleniumserver.services.teams;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.teams.TeamInvite;
import com.millenium.milleniumserver.repos.teams.TeamInvitesRepo;
import com.millenium.milleniumserver.services.auth.UserEntityService;
import com.millenium.milleniumserver.utils.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeamInvitesService {
    private TeamInvitesRepo teamInvitesRepo;
    private TeamEntityService teamEntityService;
    private UserEntityService userEntityService;
    private WebsocketUtils websocketUtils;

    public boolean createTeamInvite(String link, Integer userId) {
        TeamEntity team = teamEntityService.findTeamByInviteLink(link);
        UserEntity user = userEntityService.findUserById(userId);
        if (team != null && user != null) {
            TeamInvite joinRequest = teamInvitesRepo.save(new TeamInvite(team, user));
            websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/joinRequestUpdate", joinRequest);
            return true;
        } else {
            return false;
        }
    }

    @Autowired
    public void setTeamInvitesRepo(TeamInvitesRepo teamInvitesRepo) {
        this.teamInvitesRepo = teamInvitesRepo;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }

    @Autowired
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Autowired
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }
}
