package com.farias.mini_ecommerce.exception.exceptions.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public UserNotFoundException() {
        super("User not found.");
        this.status = HttpStatus.BAD_REQUEST;
    }
}
