package com.millenium.milleniumserver.entity.teams;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "team_invitations", schema = "millenium")
public class TeamInvite {
    private Integer inviteId;
    private TeamEntity team;
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id", nullable = false)
    public Integer getInviteId() {
        return inviteId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
    public TeamEntity getTeam() {
        return team;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public TeamInvite(TeamEntity team, UserEntity user) {
        this.team = team;
        this.user = user;
    }
}
