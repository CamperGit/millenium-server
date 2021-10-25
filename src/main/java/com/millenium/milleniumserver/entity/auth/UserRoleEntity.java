package com.millenium.milleniumserver.entity.auth;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "millenium")
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class UserRoleEntity implements GrantedAuthority {

    private Integer roleId;
    private String role;
    private Set<UserEntity> users;

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

    @Transient
    @ManyToMany(mappedBy = "roles",
            cascade = CascadeType.ALL)
    public Set<UserEntity> getUsers() {
        return users;
    }

    @Transient
    @Override
    public String getAuthority() {
        return getRole();
    }
}
