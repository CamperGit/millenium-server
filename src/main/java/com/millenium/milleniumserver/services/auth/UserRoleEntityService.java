package com.millenium.milleniumserver.services.auth;

import com.millenium.milleniumserver.entity.auth.UserRoleEntity;
import com.millenium.milleniumserver.repos.auth.UserRoleEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRoleEntityService {
    private UserRoleEntityRepo userRoleEntityRepo;

    @Transactional(readOnly = true)
    public List<UserRoleEntity> findAll() {
        return userRoleEntityRepo.findAll();
    }

    public UserRoleEntity findByRole(String role) {
        return userRoleEntityRepo.findByRole(role).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }

    @Autowired
    public void setUserRoleEntityRepo(UserRoleEntityRepo userRoleEntityRepo) {
        this.userRoleEntityRepo = userRoleEntityRepo;
    }
}
