package com.farias.mini_ecommerce.exception.handler;

import com.farias.mini_ecommerce.exception.dto.ErrorResponse;
import com.farias.mini_ecommerce.exception.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.error("Validation error: {}, {}", ex, errors);

        return ResponseEntity.badRequest().body(new ErrorResponse("Validation error.", errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        handleLog(ex);
        return handleException(ex);
    }

    @ExceptionHandler(InvalidCartException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCartException(InvalidCartException ex) {
        handleLog(ex);
        return handleException(ex);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        handleLog(ex);
        return handleException(ex);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex) {
        handleLog(ex);
        return handleException(ex);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(CartNotFoundException ex) {
        handleLog(ex);
        return handleException(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Critical error: {}", ex.getMessage());
        return handleException(ex);
    }

    private static void handleLog(RuntimeException ex ) {
        log.error("{}: {}", ex.getClass().getCanonicalName().replace("com.farias.mini_ecommerce.exception.exceptions.", ""), ex.getMessage());
    }

    private static ResponseEntity<ErrorResponse> handleException(Exception ex) {
        var errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse(ex.getClass().getCanonicalName().replace("com.farias.mini_ecommerce.exception.exceptions.", ""), errors));
    }
}
