package com.millenium.milleniumserver.controller.expenses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.payload.requests.categories.CategoryCreateRequest;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseCreateRequest;
import com.millenium.milleniumserver.services.expenses.ExpensesService;
import com.millenium.milleniumserver.utils.WebsocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public void createNewExpense(@Payload @Valid ExpenseCreateRequest expenseCreateRequest) {
        Expense expense = expensesService.createNewExpense(expenseCreateRequest);
        Category category = expense.getCategory();
        TeamEntity team = category.getTeam();
        websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/expensesUpdating", expense);
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
