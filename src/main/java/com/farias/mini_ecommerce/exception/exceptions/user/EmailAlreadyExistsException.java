package com.farias.mini_ecommerce.exception.exceptions.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailAlreadyExistsException extends RuntimeException {
    private final HttpStatus status;

    public EmailAlreadyExistsException() {
        super("Email already registered.");
        this.status = HttpStatus.CONFLICT;
    }
}
