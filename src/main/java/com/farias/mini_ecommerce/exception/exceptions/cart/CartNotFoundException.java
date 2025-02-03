package com.farias.mini_ecommerce.exception.exceptions.cart;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartNotFoundException extends BusinessException {
    public CartNotFoundException() {
        super("Current logged user doesn't have a cart.", HttpStatus.NOT_FOUND);
    }
}
