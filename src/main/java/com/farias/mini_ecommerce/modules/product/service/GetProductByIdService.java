package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class GetProductByIdService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetProductByIdService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse execute(UUID id){
        log.info("Starting to get Product by id: {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(() -> {
                            log.warn("Product with ID {} not found", id);
                            return new BusinessException("Product ID not found.", HttpStatus.BAD_REQUEST);
                        });

        log.info("Found product: {}", product);

        return productMapper.toProductResponse(product);
    }
}
