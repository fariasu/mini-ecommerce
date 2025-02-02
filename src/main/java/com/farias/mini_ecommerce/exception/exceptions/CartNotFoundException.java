package com.farias.mini_ecommerce.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public CartNotFoundException() {
        super("Current logged user doesn't have a cart.");
        this.status = HttpStatus.NOT_FOUND;
    }
}
