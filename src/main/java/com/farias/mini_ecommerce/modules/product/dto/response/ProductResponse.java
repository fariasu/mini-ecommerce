package com.farias.mini_ecommerce.modules.product.dto.response;

import java.util.List;

public record ProductResponse(
        long totalProducts,
        List<ProductShortResponse> products
) {
}
