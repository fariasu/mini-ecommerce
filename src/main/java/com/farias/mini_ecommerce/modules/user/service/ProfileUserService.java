package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.user.dto.response.UserProfileResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileUserService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileUserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProfileUserService(
            UserRepository userRepository,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserProfileResponse execute(UUID id){
        logger.info("Trying to execute profile user with id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id {}", id);
                    return new BusinessException("User ID not found.", HttpStatus.BAD_REQUEST);
                });

        logger.info("User found with id {}", user.getId());

        return userMapper.toUserProfileResponse(user);
    }
}
