package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.user.dto.request.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.response.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import com.farias.mini_ecommerce.modules.product.service.GetAllProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductsService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository,
                               UserMapper userMapper,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegisteredResponse execute(UserRegisterRequest userRequest){
        userRepository.findByEmail(userRequest.email())
                .ifPresent(user -> {
                    logger.warn("User {} already exists", userRequest.email());
                    throw new BusinessException("Email is already registered.", HttpStatus.CONFLICT);
                });

        var user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("User {} created", userRequest.email());

        return userMapper.toUserResponse(user);
    }
}
