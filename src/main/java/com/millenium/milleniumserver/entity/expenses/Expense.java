package com.millenium.milleniumserver.entity.expenses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.millenium.milleniumserver.entity.auth.TeamEntity;
import com.millenium.milleniumserver.enums.ExpensePriority;
import com.millenium.milleniumserver.enums.ExpenseState;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "expenses", schema = "millenium")
public class Expense {
    private Long expenseId;
    private String name;
    private Timestamp date;
    private ExpensePriority priority;
    private ExpenseState state;
    private String description;
    private Double fixedPrice;
    private Double minPrice;
    private Double maxPrice;
    @JsonBackReference("categories")
    private Category category;
    private TeamEntity team;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id", nullable = false)
    public Long getExpenseId() {
        return expenseId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    public ExpensePriority getPriority() {
        return priority;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public ExpenseState getState() {
        return state;
    }

    @Basic
    @Column(name = "description", length = 1000)
    public String getDescription() {
        return description;
    }

    @Basic
    @Column(name = "fixed_price")
    public Double getFixedPrice() {
        return fixedPrice;
    }

    @Basic
    @Column(name = "min_price")
    public Double getMinPrice() {
        return minPrice;
    }

    @Basic
    @Column(name = "max_price")
    public Double getMaxPrice() {
        return maxPrice;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    public Category getCategory() {
        return category;
    }

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
    public TeamEntity getTeam() {
        return team;
    }
}
