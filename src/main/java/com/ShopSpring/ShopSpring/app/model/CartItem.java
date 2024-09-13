package com.ShopSpring.ShopSpring.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Items item;  // Reference to the original Item

    private int quantity;  // Quantity of this item in the cart

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    private Cart cart;  // Reference to the Cart

}
