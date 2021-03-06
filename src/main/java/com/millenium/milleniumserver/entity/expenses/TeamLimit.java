package com.millenium.milleniumserver.entity.expenses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.millenium.milleniumserver.entity.teams.TeamEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "team_limits", schema = "millenium")
public class TeamLimit {
    private Integer limitId;
    private Double limit;
    private Integer month;
    private Integer year;
    @JsonIgnore
    private TeamEntity team;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "limit_id", nullable = false)
    public Integer getLimitId() {
        return limitId;
    }

    @Basic
    @Column(name = "limit_value", nullable = false, precision = 0)
    public Double getLimit() {
        return limit;
    }

    @Basic
    @Column(name = "month", nullable = false)
    public Integer getMonth() {
        return month;
    }

    @Basic
    @Column(name = "year", nullable = false)
    public Integer getYear() {
        return year;
    }

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
    public TeamEntity getTeam() {
        return team;
    }

    public TeamLimit(Double limit, Integer month, Integer year, TeamEntity team) {
        this.limit = limit;
        this.month = month;
        this.year = year;
        this.team = team;
    }
}
