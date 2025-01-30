package com.farias.mini_ecommerce.modules.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CartRequest(

        UUID productId,

        Integer quantity
) {}
