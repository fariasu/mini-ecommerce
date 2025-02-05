package com.farias.mini_ecommerce.security.jwt.service;

import java.time.Instant;

public record TokenResponse(
        String token,
        Instant expirationTime
) {}
