package com.farias.mini_ecommerce.modules.product.service;

import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.mapper.ProductMapper;
import com.farias.mini_ecommerce.modules.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GetAllProductsService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductResponse execute(Pageable pageable) {
        log.info("Fetching all products sorted by name in descending order.");

        var products = productRepository.findAllByOrderByNameAsc(pageable);

        if(products.isEmpty()) {
            log.info("No products found.");
            return new ProductResponse(0, List.of());
        }

        long productQuantity = productRepository.getProductsCount();

        log.info("Total products found: {}", productQuantity);
        return new ProductResponse(productQuantity, productMapper.toProductResponseList(products));
    }
}
