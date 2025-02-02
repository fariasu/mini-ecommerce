package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.user.UserNotFoundException;
import com.farias.mini_ecommerce.modules.user.dto.response.UserProfileResponse;
import com.farias.mini_ecommerce.modules.user.mapper.UserMapper;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public UserProfileResponse execute(UUID id){
        log.info("Trying get user profile with id {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        log.info("User profile found with id {}", user.getId());

        return userMapper.toUserProfileResponse(user);
    }
}
