package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartItemRepository;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CartItemDeleteService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final Validator validator;

    public CartItemDeleteService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            Validator validator
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.validator = validator;
    }

    public void execute(String userId, UUID productId) {
        log.info("Attempting to delete cart item: {} of user: {}", productId, userId);

        var userUUID = validator.validateUserId(userId);

        var cart = cartRepository.findByUserIdAndStatus(userUUID, CartStatus.OPEN)
                .orElseThrow(() -> {
                    log.warn("Current logged user:{} doesnt have a cart.", userId);
                    return new BusinessException("Current logged user doesnt have a cart.", HttpStatus.NOT_FOUND);
                });

        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("This product: {} doesnt exists inside the cart of current logged user: {}.", productId, userId);
                    return new BusinessException("This product doesnt exists inside the cart of current logged user", HttpStatus.NOT_FOUND);
                });

        cartItemRepository.deleteByProductId(cartItem.getProduct().getId());
        log.info("Cart item: {} of user: {} delete.", productId, userId);
    }
}
