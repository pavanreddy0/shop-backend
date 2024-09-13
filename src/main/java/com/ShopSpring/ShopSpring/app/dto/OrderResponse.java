package com.ShopSpring.ShopSpring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Double totalAmount;
    private int count;
}
