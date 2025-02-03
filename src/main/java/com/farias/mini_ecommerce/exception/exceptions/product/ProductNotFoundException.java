package com.farias.mini_ecommerce.exception.exceptions.product;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException() {
        super("Product not found.", HttpStatus.NOT_FOUND);
    }
}
