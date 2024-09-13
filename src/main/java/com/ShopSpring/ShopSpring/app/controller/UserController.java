package com.ShopSpring.ShopSpring.app.controller;

import com.ShopSpring.ShopSpring.app.dto.Item;
import com.ShopSpring.ShopSpring.app.dto.OrderResponse;
import com.ShopSpring.ShopSpring.app.model.Items;
import com.ShopSpring.ShopSpring.app.model.Users;
import com.ShopSpring.ShopSpring.app.service.CartService;
import com.ShopSpring.ShopSpring.app.service.ItemService;
import com.ShopSpring.ShopSpring.app.service.OrderService;
import com.ShopSpring.ShopSpring.app.service.UserService;
import com.ShopSpring.ShopSpring.app.utility.ApiResponse;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shop-spring")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    public Users authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authenticatedUser " + (Users) authentication.getPrincipal());
        return (Users) authentication.getPrincipal();
    }

    @RequestMapping("/")
    public ResponseEntity<?> getProducts(){
        try{
            List<Item> items = itemService.getAllItems();
            ApiResponse<List<Item>> response = new ApiResponse<>("Success", HttpStatus.OK, items);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/items")
    public ResponseEntity<?> saveItem(@RequestBody Item item){
        try{
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (!user.isSeller()){
                return new ResponseEntity<>("Buyer cannot add items to Available Items", HttpStatus.FORBIDDEN);
            }

            Items items = new Items();

            items.setName(item.getName());
            items.setPrice(item.getPrice());
            items.setDescription(item.getDescription());
            items.setCount(item.getCount());
            items.setSeller(user);
            itemService.saveItems(items);

            List<Item> allItems = itemService.getAllItems();
            ApiResponse<List<Item>> response = new ApiResponse<>("Success", HttpStatus.CREATED, allItems);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/items")
    public ResponseEntity<?> updateItem(@RequestBody Item item){
        try{
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (!user.isSeller()){
                return new ResponseEntity<>("Buyer cannot update items", HttpStatus.FORBIDDEN);
            }

            Items items = new Items();

            items.setId(item.getId());
            items.setName(item.getName());
            items.setPrice(item.getPrice());
            items.setDescription(item.getDescription());
            items.setCount(item.getCount());
            items.setSeller(user);

            itemService.saveItems(items);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/items")
    public ResponseEntity<?> getItems(){
        try{
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (!user.isSeller()){
                return new ResponseEntity<>("Buyer cannot see items from Seller", HttpStatus.FORBIDDEN);
            }

            List<Item> allItems = itemService.getAllItemsOfUser(user);
            ApiResponse<List<Item>> response = new ApiResponse<>("Success", HttpStatus.OK, allItems);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cart")
    public ResponseEntity<?> addItemToCart(@RequestBody Item item){
        try {
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (user.isSeller()){
                return new ResponseEntity<>("Seller cannot add items to Cart", HttpStatus.FORBIDDEN);
            }

            cartService.addItemToCart(user, item.getId(), item.getCount());
            ApiResponse<String> response = new ApiResponse<>("Success", HttpStatus.CREATED, "Success");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/cart")
    public ResponseEntity<?> getItemFromCart(){
        try {
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (user.isSeller()){
                return new ResponseEntity<>("Seller cannot have items in Cart", HttpStatus.FORBIDDEN);
            }

            List<Item> itemsInCart = cartService.getItemsInCart(user);
            ApiResponse<List<Item>> response = new ApiResponse<>("Success", HttpStatus.OK, itemsInCart);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (NoResultException e){
            ApiResponse<List<Item>> response = new ApiResponse<>("Success", HttpStatus.OK, new ArrayList<>());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PatchMapping("/cart")
//    public ResponseEntity<?> updateItemInCart(@RequestBody Item item){
//        try {
//            Users user = authenticatedUser();
//            System.out.println("User " + user);
//            if (user.isSeller()){
//                return new ResponseEntity<>("Seller cannot have items in Cart", HttpStatus.FORBIDDEN);
//            }
//
//            List<Item> itemsInCart = cartService.getItemsInCart(user);
//            ApiResponse<List<Item>> response = new ApiResponse<>("Success", HttpStatus.OK, itemsInCart);
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//        } catch (Exception e){
//            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(){
        try {
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (user.isSeller()){
                return new ResponseEntity<>("Seller cannot place Order ", HttpStatus.FORBIDDEN);
            }

            orderService.placeOrder(user.getId());

            ApiResponse<String> response = new ApiResponse<>("Success", HttpStatus.CREATED, "Success");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/order")
    public ResponseEntity<?> getOrders(){
        try {
            Users user = authenticatedUser();
            System.out.println("User " + user);
            if (user.isSeller()){
                return new ResponseEntity<>("Seller cannot place Order ", HttpStatus.FORBIDDEN);
            }

            List<OrderResponse> orderResponses = orderService.getAllOrders(user.getId());
            ApiResponse<List<OrderResponse>> response = new ApiResponse<>("Success", HttpStatus.OK, orderResponses);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
