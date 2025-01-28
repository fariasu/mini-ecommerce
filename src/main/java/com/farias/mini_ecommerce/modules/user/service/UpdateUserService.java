package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.modules.user.dto.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateUserService {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserService(UserRepository userRepository,
                               UserMapper userMapper,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegisteredResponse execute(UUID id, UserRegisterRequest userRegisterRequest) {
        logger.info("Attempting to update user with id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with id {} not found", id);
                    return new RuntimeException("User not found");
                });

        userMapper.updateUser(userRegisterRequest, user);
        user.setUpdatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("Updated user with id {}", id);

        return userMapper.toUserResponse(user);
    }
}
