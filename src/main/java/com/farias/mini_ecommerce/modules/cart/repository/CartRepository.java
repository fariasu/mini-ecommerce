package com.farias.mini_ecommerce.modules.cart.repository;

import com.farias.mini_ecommerce.modules.cart.entity.Cart;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserIdAndStatus(@Param("userId") UUID uuid, @Param("status") CartStatus cartStatus);
}
