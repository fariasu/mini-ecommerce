package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.user.dto.response.UserProfileResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProfileUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProfileUserService(
            UserRepository userRepository,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserProfileResponse execute(UUID id){
        log.info("Trying to execute profile user with id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id {}", id);
                    return new BusinessException("User ID not found.", HttpStatus.BAD_REQUEST);
                });

        log.info("User found with id {}", user.getId());

        return userMapper.toUserProfileResponse(user);
    }
}
