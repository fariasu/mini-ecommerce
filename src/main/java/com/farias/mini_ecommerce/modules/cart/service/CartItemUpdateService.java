package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.cart.CartNotFoundException;
import com.farias.mini_ecommerce.exception.exceptions.product.InsufficientStockException;
import com.farias.mini_ecommerce.exception.exceptions.product.ProductNotFoundException;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartItemUpdateRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartItemUpdatedResponse;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartItemRepository;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.UserValidator;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CartItemUpdateService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserValidator userValidator;

    @Transactional
    public CartItemUpdatedResponse execute(String userId, UUID productId, CartItemUpdateRequest cartItemUpdateRequest) {
        log.info("Executing cart item update");

        var userUUID = userValidator.validateUserId(userId);

        var cart = cartRepository.findByUserIdAndStatus(userUUID, CartStatus.OPEN)
                .orElseThrow(CartNotFoundException::new);

        var item = cart.findExistingItem(productId)
                .orElseThrow(ProductNotFoundException::new);

        var product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);


        if(cartItemUpdateRequest.newQuantity() > product.getStock()){
            throw new InsufficientStockException();
        }

        item.setQuantity(cartItemUpdateRequest.newQuantity());
        cart.updateTotalPrice();

        cartRepository.save(cart);
        cartItemRepository.save(item);
        log.info("Cart item {} updated", item.getId());

        return new CartItemUpdatedResponse(item.getQuantity());
    }
}
