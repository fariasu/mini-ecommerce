package com.farias.mini_ecommerce.service.product;

import com.farias.mini_ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteProductService {
    private static final Logger logger = LoggerFactory.getLogger(DeleteProductService.class);
    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(UUID id) throws Exception {
        logger.info("Attempting to delete product with id {}", id);

        productRepository.findById(id)
                .orElseThrow(() -> {
                logger.warn("Could not find product with id {}", id);
                return new Exception();
                });

        productRepository.deleteById(id);
    }
}
