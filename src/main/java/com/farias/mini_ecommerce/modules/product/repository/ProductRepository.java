package com.farias.mini_ecommerce.modules.product.repository;

import com.farias.mini_ecommerce.modules.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM tb_products p WHERE p.id = :uuid")
    Optional<Product> findById(@Param("uuid") UUID id);

    @Query("SELECT p FROM tb_products p ORDER BY p.name ASC")
    List<Product> findAllByOrderByNameAsc();
}
