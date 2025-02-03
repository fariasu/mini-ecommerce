package com.farias.mini_ecommerce.exception.exceptions.user;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("Email already registered.", HttpStatus.CONFLICT);
    }
}
