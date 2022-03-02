package com.millenium.milleniumserver.repo.teams;

import com.millenium.milleniumserver.entity.teams.TeamMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMessageRepo extends JpaRepository<TeamMessage, Long> {
}
