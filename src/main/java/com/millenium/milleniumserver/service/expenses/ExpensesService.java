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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.millenium.milleniumserver.specifiaction.Specifications.*;

@Service
@Transactional
public class ExpensesService {
    private ExpensesRepo expensesRepo;
    private CategoriesService categoriesService;
    private TeamLimitsService teamLimitsService;
    private TeamEntityService teamEntityService;
    private TeamMessagesService teamMessagesService;
    private WebsocketUtils websocketUtils;

    public Expense createNewExpense(ExpenseCreateRequest request) {
        Category category = categoriesService.findCategoryById(request.getCategoryId());
        Expense expense = new Expense(request.getName(), LocalDateTime.now(), request.getPriority(), ExpenseState.IN_PROCESS,
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
        expense.setDate(LocalDateTime.now());
        Expense savedExpense = expensesRepo.save(expense);
        Hibernate.initialize(savedExpense.getCategory());
        return savedExpense;
    }

    public Expense deleteExpenseById(Long expenseId) {
        Expense deletedExpense = expensesRepo.findById(expenseId).orElseThrow(EntityNotFoundException::new);
        expensesRepo.delete(deletedExpense);
        return deletedExpense;
    }

    public void checkExpensesLimit(Expense expense, Integer teamId) {
        TeamEntity team = teamEntityService.findTeamById(teamId);
        LocalDateTime localDateTime = expense.getDate();
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
                    LocalDate date = ex.getDate().toLocalDate();
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

    public List<Expense> filter(ExpensesFilterPayload filters) {
        List<Specification<Expense>> specifications = Arrays.asList(
                equalOrReturnNull("category.categoryId", filters.getCategoryId(), Expense.class),
                likeOrReturnNull("name", filters.getName(), Expense.class),
                filters.getMinPrice() != null ? Or.<Expense>builder()
                        .specifications(Arrays.asList(
                                greaterThanEqualToOrNull("fixedPrice", filters.getMinPrice(), Expense.class),
                                greaterThanEqualToOrNull("minPrice", filters.getMinPrice(), Expense.class)))
                        .build() : null,
                filters.getMaxPrice() != null ? Or.<Expense>builder()
                        .specifications(Arrays.asList(
                                lessThanEqualToOrNull("fixedPrice", filters.getMaxPrice(), Expense.class),
                                lessThanEqualToOrNull("maxPrice", filters.getMaxPrice(), Expense.class)))
                        .build() : null,
                inOrReturnNull("priority", filters.getPriorityIn(), Expense.class),
                lessThanEqualToOrNull("date", filters.getCreateDateAtTop(), Expense.class),
                greaterThanEqualToOrNull("date", filters.getCreateDateAtBottom(), Expense.class)
        );
        return expensesRepo.findAll(And.<Expense>builder()
                .specifications(specifications)
                .build());
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
