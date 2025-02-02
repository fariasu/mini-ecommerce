package com.farias.mini_ecommerce.modules.cart.service;

import com.farias.mini_ecommerce.exception.exceptions.cart.InvalidCartException;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.cart.mapper.CartMapper;
import com.farias.mini_ecommerce.modules.cart.repository.CartRepository;
import com.farias.mini_ecommerce.modules.cart.shared.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CartGetService {
    private final CartRepository cartRepository;
    private final UserValidator userValidator;
    private final CartMapper cartMapper;

    public CartGetService(
            CartRepository cartRepository,
            UserValidator userValidator,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.userValidator = userValidator;
        this.cartMapper = cartMapper;
    }

    @Transactional(readOnly = true)
    public CartResponse execute(String userId){
        var userIdUUID = userValidator.validateUserId(userId);

        var cart = cartRepository.findByUserIdAndStatus(userIdUUID, CartStatus.OPEN)
                .orElseThrow(InvalidCartException::new);

        return cartMapper.toResponse(cart);
    }
}
