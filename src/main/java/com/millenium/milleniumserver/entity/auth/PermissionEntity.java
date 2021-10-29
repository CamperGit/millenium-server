package com.millenium.milleniumserver.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@IdClass(PermissionEntityPK.class)
@Table(name = "permissions", schema = "millenium")
public class PermissionEntity implements Serializable {
    @Id
    private Integer teamId;
    @Id
    private Integer userId;
    private boolean adding;
    private boolean reading;
    private boolean changing;
    private boolean deleting;
    private boolean moderating;

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "adding", nullable = false)
    public boolean isAdding() {
        return adding;
    }

    @Basic
    @Column(name = "reading", nullable = false)
    public boolean isReading() {
        return reading;
    }

    @Basic
    @Column(name = "changing")
    public boolean isChanging() {
        return changing;
    }

    @Basic
    @Column(name = "deleting")
    public boolean isDeleting() {
        return deleting;
    }

    @Basic
    @Column(name = "moderating")
    public boolean isModerating() {
        return moderating;
    }


}