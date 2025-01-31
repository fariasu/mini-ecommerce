package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.user.dto.request.UserLoginRequest;
import com.farias.mini_ecommerce.modules.user.dto.response.UserLoggedResponse;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import com.farias.mini_ecommerce.security.jwt.service.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public UserLoggedResponse execute(UserLoginRequest userLoginRequest){
        var user = userRepository.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> {
                    log.warn("User not found {}", userLoginRequest.email());
                    return new BusinessException("Invalid username or password.", HttpStatus.BAD_REQUEST);
                });

        var passwordMatches = passwordEncoder.matches(userLoginRequest.password(), user.getPassword());
        if(!passwordMatches) {
            log.warn("Password does not match {}", userLoginRequest.email());
            throw new BusinessException("Invalid username or password.", HttpStatus.BAD_REQUEST);
        }

        log.info("Attempting to create token {}", user.getId());
        String[] rolesArray = {user.getUserRole().toString()};
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", rolesArray);
        var token = jwtTokenProvider.generateToken(user.getId(), claims);

        log.info("Token created {}", user.getId());

        return new UserLoggedResponse(token);
    }
}
