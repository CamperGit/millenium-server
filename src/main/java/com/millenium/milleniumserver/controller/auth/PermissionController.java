package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.PermissionEntity;
import com.millenium.milleniumserver.services.auth.PermissionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private PermissionEntityService permissionEntityService;

    @GetMapping
    public PermissionEntity getPermissionsByUserAndTeam(@RequestParam("teamId") Integer teamId, @RequestParam("userId") Integer userId) {
        return permissionEntityService.findPermissionByUserAndTeam(teamId, userId);
    }

    @Autowired
    public void setPermissionEntityService(PermissionEntityService permissionEntityService) {
        this.permissionEntityService = permissionEntityService;
    }
}
