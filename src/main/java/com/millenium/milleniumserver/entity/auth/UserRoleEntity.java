package com.millenium.milleniumserver.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "roles", schema = "millenium")
public class UserRoleEntity implements GrantedAuthority {

    private Integer roleId;
    private String role;
    @JsonIgnoreProperties("roles")
    private List<UserEntity> users;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    public Integer getRoleId() {
        return roleId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getRole() {
        return role;
    }

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public List<UserEntity> getUsers() {
        return users;
    }

    @Transient
    @Override
    public String getAuthority() {
        return getRole();
    }
}
