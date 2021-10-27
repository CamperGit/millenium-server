package com.millenium.milleniumserver.entity.expenses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.millenium.milleniumserver.entity.auth.TeamEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "categories", schema = "millenium")
public class Category {
    private Integer categoryId;
    private String name;
    @JsonIgnoreProperties("category")
    private List<Expense> expenses;
    @JsonIgnoreProperties("categories")
    private TeamEntity team;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    public Integer getCategoryId() {
        return categoryId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "category")
    public List<Expense> getExpenses() {
        return expenses;
    }

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
    public TeamEntity getTeam() {
        return team;
    }

    public Category(String name, List<Expense> expenses, TeamEntity team) {
        this.name = name;
        this.expenses = expenses;
        this.team = team;
    }
}
