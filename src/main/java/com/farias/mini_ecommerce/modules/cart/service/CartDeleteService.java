package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartDeleteService {
    private static final Logger logger = LoggerFactory.getLogger(CartDeleteService.class);
    private final CartRepository cartRepository;
    private final Validator validator;

    public CartDeleteService(
            CartRepository cartRepository,
            Validator validator
    ) {
        this.cartRepository = cartRepository;
        this.validator = validator;
    }

    public void execute(String userId){
    var userIdUUID = validator.validateUserId(userId);

    var cart = cartRepository.findByUserIdAndStatus(userIdUUID, CartStatus.OPEN)
            .orElseThrow(() -> {
                logger.warn("Cart with id {} not found", userIdUUID);
                return new BusinessException("Cart not found.", HttpStatus.NOT_FOUND);
            });

    cartRepository.deleteById(cart.getId());
    logger.info("Cart with id {} deleted", cart.getId());
    }
}
