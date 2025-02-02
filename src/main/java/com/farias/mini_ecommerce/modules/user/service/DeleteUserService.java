package com.farias.mini_ecommerce.modules.user.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.shared.validator.UserValidator;
import com.farias.mini_ecommerce.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DeleteUserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public DeleteUserService(
            UserRepository userRepository,
            UserValidator userValidator
    ) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Transactional
    public void execute(String uuid, UUID id) {
        log.info("Attempting to delete user with id {}", id);
        var userUUID = userValidator.validateUserId(uuid);

        if(!userUUID.equals(id))
            throw new BusinessException("Cannot delete other user.", HttpStatus.UNAUTHORIZED);

        userRepository.deleteById(id);
    }
}
