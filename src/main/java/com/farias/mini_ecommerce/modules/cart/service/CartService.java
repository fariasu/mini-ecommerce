package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.product.InsufficientStockException;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.entity.Cart;
import com.farias.mini_ecommerce.modules.cart.entity.CartItem;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.UserValidator;
import com.farias.mini_ecommerce.modules.product.entity.Product;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserValidator userValidator;
    private final com.farias.mini_ecommerce.modules.cart.mapper.CartMapper cartMapper;

    @Transactional
    public CartResponse execute(UUID productId, CartRequest cartRequest, String userId) {
        var userIdUUID = userValidator.validateUserId(userId);

        Product product = validateProduct(productId, cartRequest);
        var cart = findOrCreateCart(userIdUUID);

        addItemToCart(cart, product, cartRequest.quantity());
        cart.updateTotalPrice();

        cartRepository.save(cart);
        log.info("Item added to cart {} for user {}", cart.getId(), userIdUUID);

        return cartMapper.toResponse(cart);
    }

    private Product validateProduct(UUID productId, CartRequest cartRequest) {
        return productRepository.findById(productId)
                .filter(product -> product.getStock() >= cartRequest.quantity())
                .orElseThrow(InsufficientStockException::new);
    }

    private Cart findOrCreateCart(UUID userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .status(CartStatus.OPEN)
                            .items(new java.util.ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private void addItemToCart(Cart cart, Product product, int quantity) {
        addProduct(
                CartItem.builder()
                .product(product)
                .unitPrice(product.getPrice())
                .quantity(quantity)
                .build(), cart);
    }

    public void addProduct(CartItem newItem, Cart cart) {
        var product = newItem.getProduct();
        int requestedQuantity = newItem.getQuantity();

        if (requestedQuantity > product.getStock()) {
            throw new InsufficientStockException();
        }

        var existingItemOpt = cart.findExistingItem(product.getId());

        if (existingItemOpt.isPresent()) {
            var existingItem = existingItemOpt.get();
            int newTotalQuantity = existingItem.getQuantity() + requestedQuantity;

            if (newTotalQuantity > product.getStock()) {
                throw new InsufficientStockException();
            }
            existingItem.setQuantity(newTotalQuantity);
        } else {
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }
    }
}