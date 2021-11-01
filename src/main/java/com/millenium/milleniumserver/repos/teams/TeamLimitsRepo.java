package com.millenium.milleniumserver.repos.teams;

import com.millenium.milleniumserver.entity.expenses.TeamLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamLimitsRepo extends JpaRepository<TeamLimit, Integer> {

}
