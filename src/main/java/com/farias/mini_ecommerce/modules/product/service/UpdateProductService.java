package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.modules.product.dto.ProductRequest;
import com.farias.mini_ecommerce.modules.product.dto.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateProductService {

    private static final Logger logger = LoggerFactory.getLogger(UpdateProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public UpdateProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse execute(ProductRequest productRequest, UUID id) throws Exception {
        logger.info("Updating product with id {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(() -> {
            logger.warn("Product with id {} not found", id);
            return new Exception();
        });

        productMapper.updateProduct(productRequest, product);
        productRepository.save(product);

        logger.info("Product with id {} updated", id);

        return productMapper.toProductResponse(product);
    }
}
