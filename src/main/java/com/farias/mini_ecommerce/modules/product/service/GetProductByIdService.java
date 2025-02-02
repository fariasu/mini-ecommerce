package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.exception.exceptions.product.ProductNotFoundException;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public ProductResponse execute(UUID id){
        log.info("Starting to get Product by id: {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        log.info("Found product: {}", product);

        return productMapper.toProductResponse(product);
    }
}
