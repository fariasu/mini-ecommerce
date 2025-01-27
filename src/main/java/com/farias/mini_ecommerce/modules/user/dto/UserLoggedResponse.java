package com.farias.mini_ecommerce.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoggedResponse(
        @Schema(description = "Login token.")
        String access_token
) {}
