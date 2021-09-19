package com.millenium.milleniumserver.repos.auth;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamEntityRepo extends JpaRepository<TeamEntity, Integer> {
}
