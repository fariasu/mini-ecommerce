package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.cart.CartNotFoundException;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartDeleteService {
    private final CartRepository cartRepository;
    private final UserValidator userValidator;

    public CartDeleteService(
            CartRepository cartRepository,
            UserValidator userValidator
    ) {
        this.cartRepository = cartRepository;
        this.userValidator = userValidator;
    }

    @Transactional
    public void execute(String userId){
    var userIdUUID = userValidator.validateUserId(userId);

    var cart = cartRepository.findByUserIdAndStatus(userIdUUID, CartStatus.OPEN)
            .orElseThrow(CartNotFoundException::new);

    cartRepository.deleteById(cart.getId());
    log.info("Cart with id {} deleted", cart.getId());
    }
}
