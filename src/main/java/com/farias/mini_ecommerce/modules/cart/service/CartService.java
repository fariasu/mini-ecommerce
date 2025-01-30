package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.entity.Cart;
import com.farias.mini_ecommerce.modules.cart.entity.CartItem;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.mapper.CartMapper;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import com.farias.mini_ecommerce.modules.user.service.LoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository,
            CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    public CartResponse execute(CartRequest cartRequest, String userId) {

        var product = productRepository.findById(cartRequest.productId())
                .orElseThrow(() -> {
                logger.warn("Product with id {} not found", cartRequest.productId());
                return new BusinessException("Product doesnt exists.", HttpStatus.BAD_REQUEST);
                });

        if(product.getStock() <= 0 || product.getStock() < cartRequest.quantity()){
            throw new BusinessException("Insufficient stock.", HttpStatus.BAD_REQUEST);
        }

        var cartOpt = cartRepository.findByUserIdAndStatus(UUID.fromString(userId), CartStatus.OPEN);

        if(cartOpt.isPresent()){
            var cart = cartOpt.get();

            var cartItem = CartItem.builder()
                    .product(product)
                    .unitPrice(product.getPrice())
                    .quantity(cartRequest.quantity())
                    .build();

            cart.addProduct(cartItem);
            cart.setTotalPrice(cart.getItems()
                    .stream()
                    .mapToDouble(CartItem::getUnitPrice)
                    .sum());

            cartRepository.saveAndFlush(cart);

            return new CartResponse(cart.getId(), UUID.fromString(userId), cart.getItems(), cart.getTotalPrice(), cart.getStatus());
        }

        var cart = Cart.builder()
                .items(new ArrayList<>())
                .userId(UUID.fromString(userId))
                .status(CartStatus.OPEN)
                .id(UUID.randomUUID())
                .build();

        var cartItem = CartItem.builder()
                .product(product)
                .unitPrice(product.getPrice())
                .build();

        cart.addProduct(cartItem);
        cart.setTotalPrice(cart.getItems()
                .stream()
                .mapToDouble(CartItem::getUnitPrice)
                .sum());

        cartRepository.save(cart);

        return new CartResponse(cart.getId(), UUID.fromString(userId), cart.getItems(), cart.getTotalPrice(), cart.getStatus());
    }
}
