package com.farias.mini_ecommerce.modules.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoggedResponse(

        @Schema(description = "Token type.")
        String tokenType,

        @Schema(description = "Login token.")
        String accessToken,

        @Schema(description = "Expiration time.")
        Long expiresIn
) {}
