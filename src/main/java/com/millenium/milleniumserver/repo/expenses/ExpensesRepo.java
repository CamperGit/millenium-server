package com.millenium.milleniumserver.repo.expenses;

import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.expenses.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepo extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
}
