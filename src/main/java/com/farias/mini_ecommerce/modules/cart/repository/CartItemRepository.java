package com.farias.mini_ecommerce.modules.cart.repository;

import com.farias.mini_ecommerce.modules.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Modifying
    @Query("DELETE from tb_cart_items item WHERE item.product.id = :productId")
    void deleteByProductId(@Param("productId") UUID id);
}
