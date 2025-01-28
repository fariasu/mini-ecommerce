package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    public ProductResponse execute(UUID id){
        logger.info("Starting to get Product by id: {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(() -> {
                            logger.warn("Product with ID {} not found", id);
                            return new BusinessException("Product ID not found.", HttpStatus.BAD_REQUEST);
                        });

        logger.info("Found product: {}", product);

        return productMapper.toProductResponse(product);
    }
}
