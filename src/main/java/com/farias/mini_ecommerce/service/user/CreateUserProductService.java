package com.farias.mini_ecommerce.service.user;

import com.farias.mini_ecommerce.dto.request.UserRequest;
import com.farias.mini_ecommerce.dto.response.UserResponse;
import com.farias.mini_ecommerce.mapper.UserMapper;
import com.farias.mini_ecommerce.repository.UserRepository;
import com.farias.mini_ecommerce.service.product.GetAllProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateUserProductService {

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductsService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CreateUserProductService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse execute(UserRequest userRequest){
        userRepository.findByEmail(userRequest.email())
                .ifPresent(user -> {
                    logger.warn("User {} already exists", userRequest.email());
                    throw new IllegalArgumentException("User " + userRequest.email() + " already exists");
                });

        var user = userMapper.toUser(userRequest);
        userRepository.save(user);

        logger.info("User {} created", userRequest.email());

        return userMapper.toUserResponse(user);
    }
}
