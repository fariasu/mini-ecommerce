package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.dto.response.ResponseOrderGenerated;
import com.farias.mini_ecommerce.modules.cart.entity.CartItem;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import com.farias.mini_ecommerce.modules.product.entity.Product;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartFinalizeService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final Validator validator;

    public CartFinalizeService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            Validator validator
    ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.validator = validator;
    }

    @Transactional
    public ResponseOrderGenerated execute(String userID) {
        log.info("Executing cart finalization.");
        var userUUID = validator.validateUserId(userID);

        var cart = cartRepository.findByUserIdAndStatus(userUUID, CartStatus.OPEN)
                .orElseThrow(() -> {
                    log.warn("Cart not found for user id {}", userID);
                    return new BusinessException("Current user must have a cart.", HttpStatus.NOT_FOUND);
                });

        var cartProducts = cart.getItems()
                .stream()
                .map(CartItem::getProduct)
                .toList();

        var productIds = cartProducts.stream()
                .map(Product::getId)
                .toList();

        var products = productRepository.findAllById(productIds);

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (var item : cartProducts) {
            var product = productMap.get(item.getId());
            if (product == null) {
                log.warn("Product not found for id {}", item.getId());
                throw new BusinessException("Product not found.", HttpStatus.NOT_FOUND);
            }
            if (product.getStock() < item.getStock()) {
                log.warn("Product has not enough stock {}", item.getId());
                throw new BusinessException("Product has not enough stock.", HttpStatus.CONFLICT);
            }

            product.setStock(product.getStock() - item.getStock());
        }

        productRepository.saveAll(products);
        cart.setStatus(CartStatus.FINISHED);
        cartRepository.save(cart);

        log.info("Cart has been finalized");
        return new ResponseOrderGenerated(UUID.randomUUID());
    }
}

