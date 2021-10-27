package com.millenium.milleniumserver.entity.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "refresh_tokens", schema = "millenium")
public class RefreshTokenEntity {
    private Long tokenId;
    private UserEntity user;
    private String token;
    private Timestamp expiryTime;
    private boolean used;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    public Long getTokenId() {
        return tokenId;
    }

    @Basic
    @Column(name = "token", nullable = false, length = 1000)
    public String getToken() {
        return token;
    }

    @Basic
    @Column(name = "expiry_time", nullable = false)
    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    @Basic
    @Column(name = "used", nullable = false)
    public boolean isUsed() {
        return used;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    public UserEntity getUser() {
        return user;
    }
}
