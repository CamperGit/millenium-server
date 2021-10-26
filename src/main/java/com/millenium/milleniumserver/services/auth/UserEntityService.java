package com.millenium.milleniumserver.services.auth;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.auth.UserRoleEntity;
import com.millenium.milleniumserver.payload.responses.UserInfoResponse;
import com.millenium.milleniumserver.repos.auth.UserEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserEntityService implements UserDetailsService {
    private UserEntityRepo userEntityRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveUser(UserEntity user) {
        userEntityRepo.save(user);
    }

    public UserInfoResponse getUserInfoResponse(UserEntity userEntity) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setRoles(userEntity.getRoles().stream().map(UserRoleEntity::getRole).collect(Collectors.toList()));
        userInfoResponse.setTeams(userEntity.getTeams());
        return userInfoResponse;
    }

    @Transactional(readOnly = true)
    public UserEntity findUserById(Integer id) {return userEntityRepo.findById(id).get();}

    public boolean deleteUserById(Integer userId) {
        if (userEntityRepo.findById(userId).isPresent()) {
            userEntityRepo.deleteById(userId);
            return true;
        }
        return false;
    }

    public void deleteUser(UserEntity user) {
        userEntityRepo.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return userEntityRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return userEntityRepo.existsByUsername(username);
    }

    @Autowired
    public void setUserEntityRepository(UserEntityRepo userEntityRepo) {
        this.userEntityRepo = userEntityRepo;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
