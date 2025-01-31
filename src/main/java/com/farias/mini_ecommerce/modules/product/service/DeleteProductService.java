package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DeleteProductService {
    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(UUID id){
        log.info("Attempting to delete product with id {}", id);

        productRepository.findById(id)
                .orElseThrow(() -> {
                log.warn("Could not find product with id {}", id);
                return new BusinessException("Product ID not found.", HttpStatus.BAD_REQUEST);
                });

        productRepository.deleteById(id);
    }
}
