package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.CartNotFoundException;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartDeleteService {
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
            .orElseThrow(CartNotFoundException::new);

    cartRepository.deleteById(cart.getId());
    log.info("Cart with id {} deleted", cart.getId());
    }
}
