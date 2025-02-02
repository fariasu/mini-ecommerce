package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetAllProductsService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetAllProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> execute(Pageable pageable) {
        log.info("Fetching all products sorted by name in descending order.");

        var products = productRepository.findAllByOrderByNameAsc(pageable);

        if(products.isEmpty()) {
            log.info("No products found.");
            return List.of();
        }

        return productMapper.toProductResponseList(products);
    }
}
