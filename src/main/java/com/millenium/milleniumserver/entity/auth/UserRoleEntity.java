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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String role;

    @Transient
    @ManyToMany(mappedBy = "roles",
            cascade = CascadeType.ALL)
    private Set<UserEntity> users;

    public Integer getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
