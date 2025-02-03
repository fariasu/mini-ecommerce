package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.modules.product.dto.request.ProductRequest;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductShortResponse;
import com.farias.mini_ecommerce.modules.product.entity.Product;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CreateProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductShortResponse execute(ProductRequest productRequest) {
        log.info("Starting to create product: {}", productRequest);

        Product product = productMapper.toProduct(productRequest);

        Product savedProduct = productRepository.saveAndFlush(product);
        log.info("Product saved to database: {}", savedProduct);

        return productMapper.toProductResponse(savedProduct);
    }
}
