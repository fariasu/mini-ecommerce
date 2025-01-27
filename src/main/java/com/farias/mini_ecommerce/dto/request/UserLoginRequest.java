package com.farias.mini_ecommerce.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO User login.")
public record UserLoginRequest(
        @Email(message = "Invalid email format.")
        @NotNull(message = "The email must not be null.")
        @Schema(description = "User email.", example = "john@doe.com")
        String email,

        @Size(max = 256)
        @NotNull(message = "The password must not be null.")
        @Schema(description = "User password.", example = "Strong@Password123")
        String password
) {}
