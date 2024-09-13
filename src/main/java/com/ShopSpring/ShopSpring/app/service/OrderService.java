package com.ShopSpring.ShopSpring.app.service;

import com.ShopSpring.ShopSpring.app.dto.OrderResponse;
import com.ShopSpring.ShopSpring.app.model.Cart;
import com.ShopSpring.ShopSpring.app.model.Orders;
import com.ShopSpring.ShopSpring.app.repository.CartRepository;
import com.ShopSpring.ShopSpring.app.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Orders placeOrder(Long userId) {
        // Fetch the active cart for the user
        Cart activeCart = cartRepository.findByUserIdAndIsActive(userId, true)
                .orElseThrow(() -> new RuntimeException("Active cart not found for user"));

        // Create a new order and link the active cart
        Orders order = new Orders();
        order.setUser(activeCart.getUser());
        order.setCart(activeCart);
//        order.setStatus(OrderStatus.PENDING);

        // Calculate the total amount for the order
        double totalAmount = activeCart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getItem().getPrice() * cartItem.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        // Save the order
        Orders savedOrder = orderRepository.save(order);

        // Mark the cart as inactive after placing the order
        activeCart.setIsActive(false);
        cartRepository.save(activeCart);

        return savedOrder;
    }

    public List<OrderResponse> getAllOrders(Long userId){
        List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
        List<Orders> orders = orderRepository.findOrdersByUserId(userId);
        orders.forEach(orders1 -> orderResponses.add(new OrderResponse(orders1.getId(), orders1.getTotalAmount(), orders1.getCount())));
        return orderResponses;
    }
}
