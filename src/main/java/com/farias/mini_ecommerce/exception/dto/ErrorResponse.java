package com.farias.mini_ecommerce.exception.dto;

import java.util.List;

public record ErrorResponse(
        String title,
        List<String> errors
) {}
