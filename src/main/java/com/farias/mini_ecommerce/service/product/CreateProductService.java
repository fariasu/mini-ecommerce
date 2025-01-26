package com.farias.mini_ecommerce.service.product;

import com.farias.mini_ecommerce.domain.Product;
import com.farias.mini_ecommerce.dto.request.ProductRequest;
import com.farias.mini_ecommerce.dto.response.ProductResponse;
import com.farias.mini_ecommerce.mapper.ProductMapper;
import com.farias.mini_ecommerce.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateProductService {
    private static final Logger logger = LoggerFactory.getLogger(CreateProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CreateProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse execute(ProductRequest productRequest) {
        logger.info("Starting to create product: {}", productRequest);

        Product product = productMapper.toProduct(productRequest);

        Product savedProduct = productRepository.save(product);
        logger.info("Product saved to database: {}", savedProduct);

        return productMapper.toProductResponse(savedProduct);
    }
}
