package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.product.dto.request.ProductRequest;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    public ProductResponse execute(ProductRequest productRequest, UUID id){
        logger.info("Updating product with id {}", id);

        var product = productRepository.findById(id)
                .orElseThrow(() -> {
            logger.warn("Product with id {} not found", id);
            return new BusinessException("Product ID not found.", HttpStatus.BAD_REQUEST);
        });

        productMapper.updateProduct(productRequest, product);
        productRepository.save(product);

        logger.info("Product with id {} updated", id);

        return productMapper.toProductResponse(product);
    }
}
