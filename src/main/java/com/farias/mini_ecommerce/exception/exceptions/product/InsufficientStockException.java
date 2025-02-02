package com.farias.mini_ecommerce.exception.exceptions.product;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InsufficientStockException extends RuntimeException {
    private final HttpStatus status;

    public InsufficientStockException() {
        super("Product has not enough stock.");
        this.status = HttpStatus.CONFLICT;
    }
}
