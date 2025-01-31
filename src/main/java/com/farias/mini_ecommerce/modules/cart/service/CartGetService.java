package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.mapper.CartMapper;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartGetService {
    private final CartRepository cartRepository;
    private final Validator validator;
    private final CartMapper cartMapper;

    public CartGetService(
            CartRepository cartRepository,
            Validator validator,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.validator = validator;
        this.cartMapper = cartMapper;
    }

    public CartResponse execute(String userId){
        var userIdUUID = validator.validateUserId(userId);

        var cart = cartRepository.findByUserIdAndStatus(userIdUUID, CartStatus.OPEN)
                .orElseThrow(() -> {
                    log.warn("Cart not found for user id {}", userId);
                    return new BusinessException("Current user does not have a cart.", HttpStatus.NOT_FOUND);
                });

        return cartMapper.toResponse(cart);
    }
}
