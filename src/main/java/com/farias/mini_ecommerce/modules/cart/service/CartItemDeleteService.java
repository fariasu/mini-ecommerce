package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.cart.InvalidCartException;
import com.farias.mini_ecommerce.exception.exceptions.product.ProductNotFoundException;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartItemRepository;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CartItemDeleteService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserValidator userValidator;

    @Transactional
    public void execute(String userId, UUID productId) {
        log.info("Attempting to delete cart item: {} of user: {}", productId, userId);

        var userUUID = userValidator.validateUserId(userId);

        var cart = cartRepository.findByUserIdAndStatus(userUUID, CartStatus.OPEN)
                .orElseThrow(InvalidCartException::new);

        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(ProductNotFoundException::new);

        cartItemRepository.deleteByProductId(cartItem.getProduct().getId());
        log.info("Cart item: {} of user: {} delete.", productId, userId);
    }
}
