package com.farias.mini_ecommerce.exception.exceptions.user;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super("User not found.", HttpStatus.BAD_REQUEST);
    }
}
