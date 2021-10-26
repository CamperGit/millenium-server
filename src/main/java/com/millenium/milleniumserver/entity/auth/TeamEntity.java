package com.millenium.milleniumserver.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.millenium.milleniumserver.entity.expenses.Expense;
import com.millenium.milleniumserver.entity.expenses.TeamLimit;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "teams", schema = "millenium")
public class TeamEntity {
    private Integer teamId;
    private String name;
    @JsonIgnoreProperties("teams")
    private List<Expense> expenses;
    @JsonIgnoreProperties("teams")
    private List<TeamLimit> limits;
    @JsonIgnoreProperties("teams")
    private List<UserEntity> users;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    public Integer getTeamId() {
        return teamId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "team")
    public List<Expense> getExpenses() {
        return expenses;
    }

    @OneToMany(mappedBy = "team")
    public List<TeamLimit> getLimits() {
        return limits;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "teams_users",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public List<UserEntity> getUsers() {
        return users;
    }

    public TeamEntity(String name, List<Expense> expenses, List<TeamLimit> limits, List<UserEntity> users) {
        this.name = name;
        this.expenses = expenses;
        this.limits = limits;
        this.users = users;
    }
}
