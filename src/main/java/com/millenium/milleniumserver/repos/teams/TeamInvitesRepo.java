package com.millenium.milleniumserver.repos.teams;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.teams.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamInvitesRepo extends JpaRepository<TeamInvite, Integer> {
    List<TeamInvite> findAllByTeam(TeamEntity team);
    Boolean existsByTeamAndUser(TeamEntity team, UserEntity user);
}
