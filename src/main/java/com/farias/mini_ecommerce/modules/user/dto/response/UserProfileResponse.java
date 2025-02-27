package com.farias.mini_ecommerce.modules.user.dto.response;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String name,
        String email
) {}
