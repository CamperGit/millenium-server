package com.millenium.milleniumserver.entity.teams;

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
