package com.millenium.milleniumserver.entity.teams;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "team_messages", schema = "millenium")
public class TeamMessage {
    private Long messageId;
    private String text;
    private boolean readed;
    private TeamEntity team;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    public Long getMessageId() {
        return messageId;
    }

    @Basic
    @Column(name = "text", nullable = false, length = -1)
    public String getText() {
        return text;
    }

    @Basic
    @Column(name = "readed", nullable = false)
    public boolean isReaded() {
        return readed;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
    public TeamEntity getTeam() {
        return team;
    }

    public TeamMessage(String text, boolean readed, TeamEntity team) {
        this.text = text;
        this.readed = readed;
        this.team = team;
    }
}
