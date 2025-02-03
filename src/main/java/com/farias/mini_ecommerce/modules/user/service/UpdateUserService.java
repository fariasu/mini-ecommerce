package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.user.EmailAlreadyExistsException;
import com.farias.mini_ecommerce.exception.exceptions.user.UserNotFoundException;
import com.farias.mini_ecommerce.modules.user.dto.request.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.response.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UpdateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserRegisteredResponse execute(UUID id, UserRegisterRequest userRegisterRequest) {
        log.info("Attempting to update user with id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if(!userRegisterRequest.email().equals(user.getEmail())) {
            var emailExists = userRepository.existsByEmail(userRegisterRequest.email());

            if(emailExists)
                throw new EmailAlreadyExistsException();
        }

        user.setUpdatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userMapper.updateUser(userRegisterRequest, user);
        userRepository.save(user);

        log.info("Updated user with id {}", id);

        return userMapper.toUserResponse(user);
    }
}
