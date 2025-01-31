package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteUserService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserService.class);

    private final UserRepository userRepository;
    private final Validator validator;

    public DeleteUserService(
            UserRepository userRepository,
            Validator validator
    ) {
        this.userRepository = userRepository;
        this.validator = validator;

    }
    @Transactional
    public void execute(String uuid, UUID id) {
        logger.info("Attempting to delete user with id {}", id);
        var userUUID = validator.validateUserId(uuid);

        if(!userUUID.equals(id))
            throw new BusinessException("Cannot delete other user.", HttpStatus.UNAUTHORIZED);

        userRepository.deleteById(id);
    }
}
