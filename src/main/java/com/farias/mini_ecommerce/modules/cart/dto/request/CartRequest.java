package com.farias.mini_ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartRequest(

        @NotNull(message = "ID must not be null.")
        UUID productId,

        @Min(value = 1, message = "Quantity must be greater than 0.")
        Integer quantity
) {}
