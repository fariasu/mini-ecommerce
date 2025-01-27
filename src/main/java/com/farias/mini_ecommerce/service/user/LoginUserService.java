package com.farias.mini_ecommerce.service.user;

import com.farias.mini_ecommerce.dto.request.UserLoginRequest;
import com.farias.mini_ecommerce.dto.response.UserLoggedResponse;
import com.farias.mini_ecommerce.repository.UserRepository;
import com.farias.mini_ecommerce.service.token.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginUserService {

    private static final Logger logger = LoggerFactory.getLogger(LoginUserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserLoggedResponse execute(UserLoginRequest userLoginRequest){
        var user = userRepository.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> {
                    logger.warn("User not found {}", userLoginRequest.email());
                    return new SecurityException("User not found");
                });

        var passwordMatches = passwordEncoder.matches(userLoginRequest.password(), user.getPassword());
        if(!passwordMatches) {
            logger.warn("Password does not match {}", userLoginRequest.email());
            throw new SecurityException("Wrong email or Password.");
        }

        logger.info("Attempting to create token {}", user.getId());
        String[] rolesArray = {user.getUserRole().toString()};
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", rolesArray);
        var token = jwtService.generateToken(user.getId(), claims);

        logger.info("Token created {}", user.getId());

        return new UserLoggedResponse(token);
    }
}
