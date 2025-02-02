package com.farias.mini_ecommerce.modules.cart.shared.validator;

import com.farias.mini_ecommerce.exception.exceptions.user.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserValidator {

    public UUID validateUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException();
        }
    }
}
