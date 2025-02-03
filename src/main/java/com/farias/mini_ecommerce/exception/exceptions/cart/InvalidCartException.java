package com.farias.mini_ecommerce.exception.exceptions.cart;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidCartException extends BusinessException {
  public InvalidCartException() {
    super("Invalid cart.", HttpStatus.NOT_FOUND);
  }
}
