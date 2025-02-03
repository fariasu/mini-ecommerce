package com.farias.mini_ecommerce.exception.exceptions.product;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InsufficientStockException extends BusinessException {
    public InsufficientStockException() {
        super("Product has not enough stock.", HttpStatus.CONFLICT);
    }
}
