package com.farias.mini_ecommerce.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredResponse(
        @Schema(description = "ID of the User")
        UUID id,
        @Schema(description = "Name of the User")
        String name,

        @Schema(description = "Email of the User")
        String email,

        @Schema(description = "User creation date.")
        Instant createdAt) {
}
