package com.farias.mini_ecommerce.modules.cart.shared.validator;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public UUID validateUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid user ID format: {}", userId);
            throw new BusinessException("Unauthorized.", HttpStatus.UNAUTHORIZED);
        }
    }
}
