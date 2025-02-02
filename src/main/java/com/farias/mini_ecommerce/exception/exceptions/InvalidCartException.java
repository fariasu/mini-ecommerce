package com.farias.mini_ecommerce.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidCartException extends RuntimeException {
  private final HttpStatus status;

  public InvalidCartException() {
    super("Invalid cart.");
    this.status = HttpStatus.NOT_FOUND;
  }
}
