package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.user.dto.request.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.response.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class UpdateUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UpdateUserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegisteredResponse execute(UUID id, UserRegisterRequest userRegisterRequest) {
        log.info("Attempting to update user with id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with id {} not found", id);
                    return new BusinessException("User ID not found.", HttpStatus.BAD_REQUEST);
                });

        if(!userRegisterRequest.email().equals(user.getEmail())) {
            var emailExists = userRepository.existsByEmail(userRegisterRequest.email());

            if(emailExists)
                throw new BusinessException("Email already exists.", HttpStatus.CONFLICT);
        }

        user.setUpdatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userMapper.updateUser(userRegisterRequest, user);
        userRepository.save(user);

        log.info("Updated user with id {}", id);

        return userMapper.toUserResponse(user);
    }
}
