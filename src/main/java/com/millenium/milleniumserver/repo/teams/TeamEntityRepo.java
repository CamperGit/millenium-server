package com.millenium.milleniumserver.repo.teams;

import com.millenium.milleniumserver.entity.teams.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamEntityRepo extends JpaRepository<TeamEntity, Integer> {
    TeamEntity findTeamEntityByInviteLink(String inviteLink);
}
