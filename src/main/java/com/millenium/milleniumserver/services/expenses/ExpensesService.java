package com.millenium.milleniumserver.services.expenses;

import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.enums.ExpenseState;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseCreateRequest;
import com.millenium.milleniumserver.repos.expenses.ExpensesRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@Transactional
public class ExpensesService {
    private ExpensesRepo expensesRepo;
    private CategoriesService categoriesService;

    public Expense createNewExpense(ExpenseCreateRequest request) {
        Category category = categoriesService.findCategoryById(request.getCategoryId());
        Expense expense = new Expense(request.getName(), Timestamp.from(Instant.now()), request.getPriority(), ExpenseState.IN_PROCESS,
                request.getDescription(), request.getFixedPrice(), request.getMinPrice(), request.getMaxPrice(), category);
        Expense savedExpense = expensesRepo.save(expense);
        Hibernate.initialize(savedExpense.getCategory());
        return savedExpense;
    }

    public Expense deleteExpenseById(Long expenseId) {
        Expense deletedExpense = expensesRepo.getById(expenseId);
        expensesRepo.delete(deletedExpense);
        return deletedExpense;
    }

    @Autowired
    public void setExpensesRepo(ExpensesRepo expensesRepo) {
        this.expensesRepo = expensesRepo;
    }

    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }
}
