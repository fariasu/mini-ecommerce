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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserValidator userValidator;
    private final com.farias.mini_ecommerce.modules.cart.mapper.CartMapper cartMapper;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            com.farias.mini_ecommerce.modules.cart.mapper.CartMapper cartMapper,
            UserValidator userValidator
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.userValidator = userValidator;
    }

    @Transactional
    public CartResponse execute(UUID productId, CartRequest cartRequest, String userId) {
        var userIdUUID = userValidator.validateUserId(userId);

        Product product = validateProduct(productId, cartRequest);
        var cart = findOrCreateCart(userIdUUID);

        addItemToCart(cart, product, cartRequest.quantity());
        updateTotalPrice(cart);

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

        Optional<CartItem> existingItemOpt = findExistingItem(product.getId(), cart);

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

    private Optional<CartItem> findExistingItem(UUID productId, Cart cart) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    public void updateTotalPrice(Cart cart) {
        cart.setTotalPrice(cart.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}