package com.farias.mini_ecommerce.service.product;

import com.farias.mini_ecommerce.dto.response.ProductResponse;
import com.farias.mini_ecommerce.mapper.ProductMapper;
import com.farias.mini_ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProductByIdService {
    private static final Logger logger = LoggerFactory.getLogger(GetProductByIdService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetProductByIdService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse execute(UUID id) throws Exception {
        logger.info("Starting to get Product by id: {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(() -> {
                            logger.warn("Product with ID {} not found", id);
                            return new Exception();
                        });

        logger.info("Found product: {}", product);

        return productMapper.toProductResponse(product);
    }
}
