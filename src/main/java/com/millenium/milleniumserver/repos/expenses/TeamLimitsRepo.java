package com.millenium.milleniumserver.repos.expenses;

import com.millenium.milleniumserver.entity.expenses.TeamLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamLimitsRepo extends JpaRepository<TeamLimit, Integer> {
}
