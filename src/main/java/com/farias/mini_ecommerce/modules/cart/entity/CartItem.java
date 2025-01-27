package com.farias.mini_ecommerce.modules.cart.entity;

import com.farias.mini_ecommerce.modules.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Entity(name = "tb_cart_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(1)
    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;
}
