package com.farias.mini_ecommerce.modules.cart.repository;

import com.farias.mini_ecommerce.modules.cart.entity.Cart;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<List<Cart>> findByUserId(UUID userId);


    Optional<Cart> findByUserIdAndStatus(UUID uuid, CartStatus cartStatus);
}
