package com.millenium.milleniumserver.service.expenses;

import com.millenium.milleniumserver.entity.expenses.Category;
import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.entity.expenses.TeamLimit;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import com.millenium.milleniumserver.entity.teams.TeamMessage;
import com.millenium.milleniumserver.enums.ExpenseState;
import com.millenium.milleniumserver.payload.requests.expenses.ExpensesFilterPayload;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseCreateRequest;
import com.millenium.milleniumserver.payload.requests.expenses.ExpenseEditRequest;
import com.millenium.milleniumserver.repo.expenses.ExpensesRepo;
import com.millenium.milleniumserver.service.teams.TeamEntityService;
import com.millenium.milleniumserver.service.teams.TeamLimitsService;
import com.millenium.milleniumserver.service.teams.TeamMessagesService;
import com.millenium.milleniumserver.specifiaction.ExpensesSpecifications;
import com.millenium.milleniumserver.specifiaction.Specifications;
import com.millenium.milleniumserver.util.WebsocketUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.millenium.milleniumserver.specifiaction.Specifications.likeOrReturnNull;

@Service
@Transactional
public class ExpensesService {
    private ExpensesRepo expensesRepo;
    private CategoriesService categoriesService;
    private TeamLimitsService teamLimitsService;
    private TeamEntityService teamEntityService;
    private TeamMessagesService teamMessagesService;
    private WebsocketUtils websocketUtils;

    public List<Expense> getExpensesByFilter(ExpensesFilterPayload filters) {
        List<Specification<Category>> specifications = Arrays.asList(
                likeOrReturnNull("name", filters.getName(), Category.class),
                ExpensesSpecifications.<Expense>equalCategoryInAssigneeOrReturnNull("executor.userId", filters.getExecutor(), filters.getAssigneeIds())
                /*lessThanEqualToOrNull("expenses.fixedPrice", filters.getMaxPrice(), Category.class),
                greaterThanEqualToOrNull("expenses.fixedPrice", filters.getMinPrice(), Category.class)*/
        );

        return categoriesRepo.findAll(
                Specifications.And.<Category>builder()
                        .specifications(specifications)
                        .build()
        );
    }

    public Expense createNewExpense(ExpenseCreateRequest request) {
        Category category = categoriesService.findCategoryById(request.getCategoryId());
        Expense expense = new Expense(request.getName(), Timestamp.from(Instant.now()), request.getPriority(), ExpenseState.IN_PROCESS,
                request.getDescription(), request.getFixedPrice(), request.getMinPrice(), request.getMaxPrice(), category);
        Expense savedExpense = expensesRepo.save(expense);
        Hibernate.initialize(savedExpense.getCategory());
        return savedExpense;
    }

    public Expense updateExpense(ExpenseEditRequest request) {
        Expense expense = expensesRepo.getById(request.getExpenseId());
        Category category = categoriesService.findCategoryById(request.getCategoryId());
        expense.setCategory(category);
        expense.setName(request.getName());
        expense.setDescription(request.getDescription());
        expense.setPriority(request.getPriority());
        expense.setFixedPrice(request.getFixedPrice());
        expense.setMinPrice(request.getMinPrice());
        expense.setMaxPrice(request.getMaxPrice());
        expense.setDate(Timestamp.from(Instant.now()));
        Expense savedExpense = expensesRepo.save(expense);
        Hibernate.initialize(savedExpense.getCategory());
        return savedExpense;
    }

    public Expense deleteExpenseById(Long expenseId) {
        Expense deletedExpense = expensesRepo.getById(expenseId);
        expensesRepo.delete(deletedExpense);
        return deletedExpense;
    }

    public void checkExpensesLimit(Expense expense, Integer teamId) {
        TeamEntity team = teamEntityService.findTeamById(teamId);
        LocalDateTime localDateTime = expense.getDate().toLocalDateTime();
        LocalDate dateOfExpense = localDateTime.toLocalDate();
        int month = dateOfExpense.getMonthValue();
        int year = dateOfExpense.getYear();
        TeamLimit teamLimit = teamLimitsService.getTeamLimitByMonthAndYear(year, month - 1, team);
        if (teamLimit != null) {
            List<Expense> monthExpenses = getTeamMonthExpenses(team, year, month, dateOfExpense.lengthOfMonth());
            Double sumOfMonthExpenses = monthExpenses.stream().mapToDouble(Expense::getApproximatePrice).sum();
            Double limit = teamLimit.getLimit();
            if (limit < sumOfMonthExpenses) {
                List<Expense> expensesToDelete = proposeExpensesToRemoval(monthExpenses, limit, sumOfMonthExpenses);
                String message = createTeamMessage(expensesToDelete, expense);
                TeamMessage teamMessage = teamMessagesService.saveTeamMessage(new TeamMessage(message, true, team));
                websocketUtils.sendMessageToUsers(team.getUsers(), "/queue/teamMessagesUpdate", teamMessage);
            }
        }
    }

    private List<Expense> getTeamMonthExpenses(TeamEntity team, Integer year, Integer month, Integer monthLength) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = LocalDate.of(year, month, monthLength);
        return team.getCategories().stream()
                .map(Category::getExpenses)
                .flatMap(Collection::stream)
                .filter(ex -> {
                    LocalDate date = ex.getDate().toLocalDateTime().toLocalDate();
                    return (date.isAfter(startOfMonth) || date.equals(startOfMonth)) &&
                            (date.isBefore(endOfMonth) || date.equals(endOfMonth));
                })
                .collect(Collectors.toList());
    }

    private String createTeamMessage(List<Expense> expensesToRemoval, Expense newExpense) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("При добавлении/изменении расхода: ")
                .append(newExpense.getName())
                .append(" вы вышли за лимит финансов для текущего месяца.")
                .append("Чтобы это исправить попробуйте изменить или удалить следующие расходы: ");
        for (Expense expenseToDelete : expensesToRemoval) {
            messageBuilder.append(expenseToDelete.getName())
                    .append("(")
                    .append(expenseToDelete.getApproximatePrice())
                    .append("), ");
        }
        return messageBuilder.substring(0, messageBuilder.length());
    }

    private List<Expense> proposeExpensesToRemoval(List<Expense> expenses, final Double limit, Double sum) {
        List<Expense> expensesToDelete = new ArrayList<>();
        expenses.sort(Comparator.comparing(Expense::getApproximatePrice).reversed());
        expenses.sort(Comparator.comparing(Expense::getPriority));
        Iterator<Expense> expensesIterator = expenses.iterator();
        while (limit < sum && expensesIterator.hasNext()) {
            Expense expenseToDelete = expensesIterator.next();
            expensesToDelete.add(expenseToDelete);
            expensesIterator.remove();
            sum -= expenseToDelete.getApproximatePrice();
        }
        return expensesToDelete;
    }

    @Autowired
    public void setTeamLimitsService(TeamLimitsService teamLimitsService) {
        this.teamLimitsService = teamLimitsService;
    }

    @Autowired
    public void setExpensesRepo(ExpensesRepo expensesRepo) {
        this.expensesRepo = expensesRepo;
    }

    @Autowired
    public void setCategoriesService(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Autowired
    public void setTeamEntityService(TeamEntityService teamEntityService) {
        this.teamEntityService = teamEntityService;
    }

    @Autowired
    public void setTeamMessagesService(TeamMessagesService teamMessagesService) {
        this.teamMessagesService = teamMessagesService;
    }

    @Autowired
    public void setWebsocketUtils(WebsocketUtils websocketUtils) {
        this.websocketUtils = websocketUtils;
    }
}
