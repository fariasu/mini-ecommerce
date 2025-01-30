package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.entity.Cart;
import com.farias.mini_ecommerce.modules.cart.entity.CartItem;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.product.entity.Product;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final com.farias.mini_ecommerce.modules.cart.mapper.CartMapper cartMapper;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            com.farias.mini_ecommerce.modules.cart.mapper.CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    @Transactional
    public CartResponse execute(CartRequest cartRequest, String userId) {
        UUID userIdUUID = validateUserId(userId);
        validateQuantity(cartRequest.quantity());

        Product product = validateProduct(cartRequest);
        Cart cart = findOrCreateCart(userIdUUID);

        addItemToCart(cart, product, cartRequest.quantity());
        cart.updateTotalPrice();

        cartRepository.save(cart);
        logger.info("Item added to cart {} for user {}", cart.getId(), userIdUUID);

        return cartMapper.toResponse(cart);
    }

    private UUID validateUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid user ID format: {}", userId);
            throw new BusinessException("Invalid user ID", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            logger.warn("Invalid quantity requested: {}", quantity);
            throw new BusinessException("Quantity must be greater than zero", HttpStatus.BAD_REQUEST);
        }
    }

    private Product validateProduct(CartRequest cartRequest) {
        return productRepository.findById(cartRequest.productId())
                .filter(product -> product.getStock() >= cartRequest.quantity())
                .orElseThrow(() -> {
                    logger.warn("Product {} unavailable or not found", cartRequest.productId());
                    return new BusinessException("Product unavailable", HttpStatus.BAD_REQUEST);
                });
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
        cart.addProduct(
                CartItem.builder()
                .product(product)
                .unitPrice(product.getPrice())
                .quantity(quantity)
                .build());
    }
}