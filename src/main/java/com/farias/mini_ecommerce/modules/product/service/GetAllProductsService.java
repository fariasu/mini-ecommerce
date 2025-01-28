package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllProductsService {
    private static final Logger logger = LoggerFactory.getLogger(GetAllProductsService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetAllProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> execute() {
        logger.info("Fetching all products sorted by name in descending order.");

        var products = productRepository.findAllByOrderByNameDesc();

        if(products.isEmpty()) {
            logger.warn("No products found.");
            return List.of();
        }

        return productMapper.toProductResponseList(products);
    }
}
