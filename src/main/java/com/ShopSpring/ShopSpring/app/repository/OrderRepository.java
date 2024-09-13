package com.ShopSpring.ShopSpring.app.repository;

import com.ShopSpring.ShopSpring.app.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    public List<Orders> findOrdersByUserId(Long user_id);
}
