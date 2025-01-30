package com.farias.mini_ecommerce.modules.cart.entity;

import com.farias.mini_ecommerce.exception.exceptions.BusinessException;
import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity(name = "tb_carts")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "status"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    private BigDecimal totalPrice;

    private CartStatus status;

    public void addProduct(CartItem newItem) {
        var product = newItem.getProduct();
        int requestedQuantity = newItem.getQuantity();

        if (requestedQuantity > product.getStock()) {
            throw new BusinessException("Insufficient stock for product: " + product.getId(),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<CartItem> existingItemOpt = findExistingItem(product.getId());

        if (existingItemOpt.isPresent()) {
            var existingItem = existingItemOpt.get();
            int newTotalQuantity = existingItem.getQuantity() + requestedQuantity;

            if (newTotalQuantity > product.getStock()) {
                throw new BusinessException("Exceeds available stock. Current stock: " + product.getStock(),
                        HttpStatus.BAD_REQUEST);
            }
            existingItem.setQuantity(newTotalQuantity);
        } else {
            newItem.setCart(this);
            this.items.add(newItem);
        }
    }

    private Optional<CartItem> findExistingItem(UUID productId) {
        return this.items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    public void updateTotalPrice() {
        this.totalPrice = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
