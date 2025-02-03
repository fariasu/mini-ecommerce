package com.farias.mini_ecommerce.exception.exceptions.user;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserUnauthorizedException extends BusinessException {
    public UserUnauthorizedException() {
        super("Unauthorized.", HttpStatus.UNAUTHORIZED);
    }
}
