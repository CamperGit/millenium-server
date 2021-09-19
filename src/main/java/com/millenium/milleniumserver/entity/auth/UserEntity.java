package com.millenium.milleniumserver.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "users", schema = "millenium")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Basic
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<UserRoleEntity> roles;

    @JsonIgnoreProperties("users")
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id", nullable = false)
    private TeamEntity team;

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }

    public Set<UserRoleEntity> getRoles() {
        return roles;
    }

    public TeamEntity getTeam() {
        return team;
    }
}
