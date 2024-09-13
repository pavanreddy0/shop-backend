package com.ShopSpring.ShopSpring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item {
    private Long id;
    private String name;
    private int price;
    private String description;
    private String image;
    private int count;
}
