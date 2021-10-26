package com.millenium.milleniumserver.controller.expenses;

import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseCreateRequest;
import com.millenium.milleniumserver.services.expenses.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private ExpensesService expensesService;

    @PostMapping
    public Expense createNewExpense(@Valid @RequestBody ExpenseCreateRequest expenseCreateRequest) {
        return expensesService.createNewExpense(expenseCreateRequest);
    }

    @Autowired
    public void setExpensesService(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }
}
