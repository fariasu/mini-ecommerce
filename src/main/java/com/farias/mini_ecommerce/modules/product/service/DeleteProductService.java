package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.exception.exceptions.product.ProductNotFoundException;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class DeleteProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void execute(UUID id) {
        log.info("Attempting to delete product with id {}", id);

        productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.deleteById(id);
    }
}
