package com.millenium.milleniumserver.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Table(name = "users", schema = "millenium")
public class UserEntity implements UserDetails {

    private Integer userId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    @JsonIgnoreProperties("users")
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List<UserRoleEntity> roles;
    //@LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("users")
    private List<TeamEntity> teams;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    public Integer getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 16)
    public String getUsername() {
        return username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 500)
    @Override
    public String getPassword() {
        return password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    @ManyToMany
    @JoinTable(name = "teams_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    public List<TeamEntity> getTeams() {
        return teams;
    }

    public UserEntity(String username, String password, String email, List<TeamEntity> teams) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.teams = teams;
    }
}
