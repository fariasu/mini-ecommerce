package com.farias.mini_ecommerce.exception.exceptions.user;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidUsernameOrPasswordException extends BusinessException {
    public InvalidUsernameOrPasswordException() {
        super("Invalid username or password.", HttpStatus.UNAUTHORIZED);
    }
}
