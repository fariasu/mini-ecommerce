package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.product.InsufficientStockException;
import com.farias.mini_ecommerce.exception.exceptions.cart.InvalidCartException;
import com.farias.mini_ecommerce.exception.exceptions.product.ProductNotFoundException;
import com.farias.mini_ecommerce.modules.cart.dto.response.ResponseOrderGenerated;
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

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CartFinalizeService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserValidator userValidator;

    @Transactional
    public ResponseOrderGenerated execute(String userID) {
        log.info("Executing cart finalization.");
        var userUUID = userValidator.validateUserId(userID);

        var cart = cartRepository.findByUserIdAndStatus(userUUID, CartStatus.OPEN)
                .orElseThrow(InvalidCartException::new);

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
                throw new ProductNotFoundException();
            }
            if (product.getStock() < item.getStock()) {
                throw new InsufficientStockException();
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

