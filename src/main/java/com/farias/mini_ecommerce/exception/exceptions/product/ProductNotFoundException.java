package com.farias.mini_ecommerce.exception.exceptions.product;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public ProductNotFoundException() {
        super("Product not found.");
        this.status = HttpStatus.NOT_FOUND;
    }
}
