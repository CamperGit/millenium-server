package com.millenium.milleniumserver.repos.teams;

import com.millenium.milleniumserver.entity.teams.TeamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamInvitesRepo extends JpaRepository<TeamInvite, Integer> {

}
