package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class DeleteUserService {
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
        log.info("Attempting to delete user with id {}", id);
        var userUUID = validator.validateUserId(uuid);

        if(!userUUID.equals(id))
            throw new BusinessException("Cannot delete other user.", HttpStatus.UNAUTHORIZED);

        userRepository.deleteById(id);
    }
}
