package com.millenium.milleniumserver.entity.auth;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntityPK implements Serializable {
    protected Integer teamId;
    protected Integer userId;
}
