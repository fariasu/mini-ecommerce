package com.farias.mini_ecommerce.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response DTO for created Product.")
public record ProductResponse(

        @Schema(description = "ID of the Product")
        UUID id,

        @Schema(description = "Name of the Product.")
        String name,

        @Schema(description = "Description of the Product.")
        String description,

        @Schema(description = "Price of the Product.")
        Double price,

        @Schema(description = "Stock of the Product.")
        Integer stock
) {}
