package com.millenium.milleniumserver.payload.responses.teams;

import com.millenium.milleniumserver.entity.teams.PermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionsResponse {
    private PermissionEntity permissions;
    private String username;
}
