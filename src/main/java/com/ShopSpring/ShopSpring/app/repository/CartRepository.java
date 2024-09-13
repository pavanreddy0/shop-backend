package com.ShopSpring.ShopSpring.app.repository;

import com.ShopSpring.ShopSpring.app.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndIsActive(Long userId, Boolean isActive);

//    public List<Cart> deleteCartBy
}
