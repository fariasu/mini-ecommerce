package com.farias.mini_ecommerce.modules.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request DTO for the Product creation.")
public record ProductRequest(
        @NotBlank(message = "The name must not be blank.")
        @Schema(description = "Name of the Product.", example = "IPhone 15")
        String name,

        @NotBlank(message = "The description must not be blank.")
        @Schema(description = "Description of the Product.", example = "Camera 10MP, 128GB")
        String description,

        @DecimalMin("0.01")
        @Schema(description = "Price of the Product.", example = "5499.99")
        Double price,

        @Min(1)
        @Schema(description = "Stock of the Product.", example = "1")
        Integer stock
) {}
