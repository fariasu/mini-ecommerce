package com.farias.mini_ecommerce.modules.cart.entity;

import com.farias.mini_ecommerce.modules.cart.entity.enums.CartStatus;
import com.farias.mini_ecommerce.modules.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "tb_carts")
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

    private Double totalPrice;

    private CartStatus status;

    public void addProduct(CartItem cartItem){

        if(this.items.isEmpty()){
            this.items.add(cartItem);
            cartItem.setCart(this);
            return;
        }

        for (CartItem item : this.items){
            if(item.getProduct().getId().equals(cartItem.getProduct().getId())){
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                this.items.add(cartItem);
                cartItem.setCart(this);
                return;
            }
        }
    }

    public void removeProduct(CartItem cartItem){
        this.items.remove(cartItem);

        cartItem.setCart(null);
    }
}
