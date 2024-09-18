package com.example.server.domain.cart.entity;

import com.example.server.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartProductId;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer productCount = 1;

    public void addCart(Cart cart){
        this.cart = cart;
        if(!cart.getCartProductList().contains(this)){
            cart.getCartProductList().add(this);
        }
    }
}
