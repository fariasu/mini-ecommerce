package com.farias.mini_ecommerce.modules.cart.dto.response;

import com.farias.mini_ecommerce.modules.cart.entity.CartItem;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        UUID userId,
        List<CartItem> cartItems,
        BigDecimal totalPrice,
        CartStatus status
) {}
