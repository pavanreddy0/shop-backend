package com.ShopSpring.ShopSpring.app.repository;

import com.ShopSpring.ShopSpring.app.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItem, Long> {
}
