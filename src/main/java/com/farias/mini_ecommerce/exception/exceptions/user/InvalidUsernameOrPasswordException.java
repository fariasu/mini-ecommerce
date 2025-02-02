package com.farias.mini_ecommerce.exception.exceptions.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidUsernameOrPasswordException extends RuntimeException {
    private final HttpStatus status;

    public InvalidUsernameOrPasswordException() {
        super("Invalid username or password.");
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
