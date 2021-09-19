package com.millenium.milleniumserver.repos.expenses;

import com.millenium.milleniumserver.entity.expenses.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepo extends JpaRepository<Expense, Long> {
}
