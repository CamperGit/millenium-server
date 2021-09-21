package com.millenium.milleniumserver.controller.auth;

import com.millenium.milleniumserver.entity.auth.RefreshTokenEntity;
import com.millenium.milleniumserver.entity.auth.UserEntity;
import com.millenium.milleniumserver.entity.auth.UserRoleEntity;
import com.millenium.milleniumserver.exceptions.TokenException;
import com.millenium.milleniumserver.jwt.JwtUtils;
import com.millenium.milleniumserver.payload.requests.LoginRequest;
import com.millenium.milleniumserver.payload.requests.SignupRequest;
import com.millenium.milleniumserver.payload.requests.TokenRefreshRequest;
import com.millenium.milleniumserver.payload.responses.JwtResponse;
import com.millenium.milleniumserver.payload.responses.MessageResponse;
import com.millenium.milleniumserver.payload.responses.TokenRefreshResponse;
import com.millenium.milleniumserver.services.auth.RefreshTokenService;
import com.millenium.milleniumserver.services.auth.UserEntityService;
import com.millenium.milleniumserver.services.auth.UserRoleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "${crossOrigin.url}", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {


    private AuthenticationManager authenticationManager;
    private UserEntityService userEntityService;
    private UserRoleEntityService userRoleEntityService;
    private RefreshTokenService refreshTokenService;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        List<String> roles = userEntity.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String accessToken = jwtUtils.generateAccessTokenFromUsername(userEntity.getUsername());

        refreshTokenService.disableRefreshToken(userEntity);
        RefreshTokenEntity newRefreshToken = refreshTokenService.createRefreshToken(userEntity);

        return ResponseEntity.ok(new JwtResponse(accessToken,
                newRefreshToken.getToken(),
                userEntity.getUserId(),
                userEntity.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userEntityService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        UserEntity user = new UserEntity(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<UserRoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            UserRoleEntity userRole = userRoleEntityService.findByRole("ROLE_USER");
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserRoleEntity adminRole = userRoleEntityService.findByRole("ROLE_ADMIN");
                        roles.add(adminRole);
                        break;
                    case "mod":
                        UserRoleEntity modRole = userRoleEntityService.findByRole("ROLE_MODERATOR");
                        roles.add(modRole);
                        break;
                    default:
                        UserRoleEntity userRole = userRoleEntityService.findByRole("ROLE_USER");
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userEntityService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByToken(requestRefreshToken).orElseThrow(()->
                new TokenException(requestRefreshToken,
                        "Refresh token is not in database!"));
        refreshTokenEntity = refreshTokenService.checkUsage(refreshTokenEntity);
        boolean valid = jwtUtils.validateJwtToken(requestRefreshToken);

        UserEntity user = refreshTokenEntity.getUser();
        refreshTokenService.disableRefreshToken(refreshTokenEntity);

        if (valid) {
            RefreshTokenEntity newRefreshToken = refreshTokenService.createRefreshToken(user);
            String accessToken = jwtUtils.generateAccessTokenFromUsername(user.getUsername());
            return ResponseEntity.ok(new TokenRefreshResponse(accessToken, newRefreshToken.getToken()));
        } else {
            throw new TokenException(requestRefreshToken, "Invalid refresh token");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/userRoles")
    public List<String> getUserRolesById(@RequestParam Integer userId) {
        UserEntity user = userEntityService.findUserById(userId);
        return user.getRoles().stream().map(UserRoleEntity::getRole).collect(Collectors.toList());
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserEntityService(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Autowired
    public void setUserRoleEntityService(UserRoleEntityService userRoleEntityService) {
        this.userRoleEntityService = userRoleEntityService;
    }

    @Autowired
    public void setRefreshTokenService(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

}