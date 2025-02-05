package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.user.EmailAlreadyExistsException;
import com.farias.mini_ecommerce.modules.user.dto.request.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.response.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.entity.enums.UserRole;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserRegisteredResponse execute(UserRegisterRequest userRequest) {
        log.info("Executing user register request: {}", userRequest);

        userRepository.findByEmail(userRequest.email())
                .ifPresent(x -> { throw new EmailAlreadyExistsException(); });

        var user = userMapper.toUser(userRequest);
        //Debug purposes
        if(Boolean.TRUE.equals(userRequest.isAdmin())){
            user.setUserRole(UserRole.ROLE_ADMIN);
        }else{
            user.setUserRole(UserRole.ROLE_USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);

        log.info("User {} created", userRequest.email());

        return userMapper.toUserResponse(user);
    }
}
