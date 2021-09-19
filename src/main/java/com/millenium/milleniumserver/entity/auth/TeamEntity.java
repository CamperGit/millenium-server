package com.millenium.milleniumserver.entity.auth;

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
    @JsonManagedReference
    private List<Expense> expenses;
    @JsonManagedReference
    private List<TeamLimit> limits;
    @JsonManagedReference
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

    @OneToMany(mappedBy = "team")
    public List<UserEntity> getUsers() {
        return users;
    }
}
