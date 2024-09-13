package com.ShopSpring.ShopSpring.app.dto;


import lombok.Data;

@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String name;
    private boolean seller;
}
