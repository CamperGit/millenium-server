package com.millenium.milleniumserver.controller.expenses;

import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseCreateRequest;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseEditRequest;
import com.millenium.milleniumserver.service.expenses.ExpensesService;
import com.millenium.milleniumserver.util.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private ExpensesService expensesService;
    private WebsocketUtils websocketUtils;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/createExpense")
    public void createNewExpense(@Payload @Valid ExpenseCreateRequest request) {
        Expense expense = expensesService.createNewExpense(request);
        Category category = expense.getCategory();
        TeamEntity team = category.getTeam();
        websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/expensesUpdating", expense);
        expensesService.checkExpensesLimit(expense, request.getTeamId());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/editExpense")
    public void editExpense(@Payload @Valid ExpenseEditRequest request) {
        Expense expense = expensesService.updateExpense(request);
        Category category = expense.getCategory();
        TeamEntity team = category.getTeam();
        websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/expensesUpdating", expense);
        expensesService.checkExpensesLimit(expense, request.getTeamId());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @MessageMapping("/deleteExpense")
    public void deleteExpense(@Payload Long expenseId) {
        Expense expense = expensesService.deleteExpenseById(expenseId);
        Category category = expense.getCategory();
        TeamEntity team = category.getTeam();
        websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/deletedExpenses", expense);
    }

    @Autowired
    public void setExpensesService(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @Autowired
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }
}
