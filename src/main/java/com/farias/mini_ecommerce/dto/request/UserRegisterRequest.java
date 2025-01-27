package com.farias.mini_ecommerce.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UserRegisterRequest(

        @Size(max = 128)
        @NotBlank(message = "The name must not be blank.")
        @Schema(description = "Name of the User.", example = "John Doe")
        String name,

        @Email
        @Size(max = 128)
        @NotBlank(message = "The email must not be blank.")
        @Schema(description = "Name of the Product.", example = "john@doe.com")
        String email,

        @Size(max = 256)
        @NotNull(message = "The password must not be blank.")
        @Schema(description = "Name of the Product.", example = "Strong@Password123")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "The password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
        String password) {
}
