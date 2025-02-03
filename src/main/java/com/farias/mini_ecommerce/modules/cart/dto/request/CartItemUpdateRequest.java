package com.farias.mini_ecommerce.modules.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Request DTO update Cart Item.")
public record CartItemUpdateRequest(

        @Min(value = 1, message = "Quantity must be greater than 0.")
        int newQuantity
) {
}
