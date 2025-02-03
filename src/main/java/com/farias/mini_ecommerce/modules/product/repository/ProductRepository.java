package com.farias.mini_ecommerce.modules.product.repository;

import com.farias.mini_ecommerce.modules.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findById(UUID id);

    List<Product> findAllByOrderByNameAsc(Pageable pageable);

    @Query("SELECT COUNT(p) FROM tb_products p")
    long getProductsCount();
}
