package com.ShopSpring.ShopSpring.app.service;

import com.ShopSpring.ShopSpring.app.dto.Item;
import com.ShopSpring.ShopSpring.app.model.Cart;
import com.ShopSpring.ShopSpring.app.model.CartItem;
import com.ShopSpring.ShopSpring.app.model.Items;
import com.ShopSpring.ShopSpring.app.model.Users;
import com.ShopSpring.ShopSpring.app.repository.CartItemsRepository;
import com.ShopSpring.ShopSpring.app.repository.CartRepository;
import com.ShopSpring.ShopSpring.app.repository.ItemsRepository;
import com.ShopSpring.ShopSpring.app.repository.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ItemsRepository itemsRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<Item> updateItemInCart(Users users, Items items, int newQuantity){
        Cart cart = cartRepository.findByUserIdAndIsActive(users.getId(), true)
                .orElseThrow(() -> new RuntimeException("Active cart not found for user"));

        Optional<CartItem> cartItemToUpdate = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(items.getId()))
                .findFirst();

        if (cartItemToUpdate.isPresent()) {
            CartItem cartItem = cartItemToUpdate.get();

            // Update the quantity of the CartItem
            if (newQuantity <= 0) {
                throw new RuntimeException("Quantity should be greater than zero");
            }
            if (newQuantity > items.getCount()) {
                throw new RuntimeException("Quantity should not be greater than available count");
            }

            cartItem.setQuantity(newQuantity);


            // Save the updated cart
            cartItemsRepository.save(cartItem);  // Save the updated CartItem

            cartRepository.save(cart);  // Save the updated cart
            return getItemsInCart(users);
        } else {
            throw new RuntimeException("Item not found in cart");
        }
    }

    @Transactional
    public List<Item> deleteItemFromCart(Users users, Items items) {
        // Find the active cart for the user
        Cart cart = cartRepository.findByUserIdAndIsActive(users.getId(), true)
                .orElseThrow(() -> new RuntimeException("Active cart not found for user"));

        // Find and remove the CartItem from the cart
        cart.getCartItems().removeIf(cartItem -> cartItem.getItem().getId().equals(items.getId()));

        // Save the updated cart
        cartRepository.save(cart);

        return getItemsInCart(users);
    }

    @Transactional
    public List<Item> addItemToCart(Users users, Long itemId, int quantity) {
        // Fetch the active cart or create a new one if not present
        Cart cart = cartRepository.findByUserIdAndIsActive(users.getId(), true)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(users);
                    newCart.setIsActive(true);
                    return cartRepository.save(newCart);
                });

        // Find the item from the database
        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        System.out.println("item " + item);

        // Check if the item already exists in the cart

        Optional<CartItem> existingCartItem = null;
        System.out.println("cart " + cart);
        if(cart.getCartItems() != null){
            existingCartItem = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                    .findFirst();
        }

        if (existingCartItem != null && existingCartItem.isPresent()) {
            // If the item already exists, just update the quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(quantity);
            cartItemsRepository.save(cartItem);
        } else {
            // If the item does not exist, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);

            // Add the new CartItem to the cart
            cart.setCartItems(new ArrayList<>());
            cart.getCartItems().add(cartItem);

            // Save the new CartItem
            cartItemsRepository.save(cartItem);
        }

        // Save the cart with the new or updated CartItem
        cartRepository.save(cart);
        return getItemsInCart(users);
    }

    @Transactional
    public List<Item> getItemsInCart(Users users){
        Cart cartItem = cartRepository.findByUserIdAndIsActive(users.getId(), true)
                .orElseThrow(() -> new NoResultException("Active cart not found for user"));

        List<CartItem> cartItems = cartItem.getCartItems();

        List<Item> items = new ArrayList<Item>();
        cartItems.forEach(cart -> items.
                add(new Item(
                        cart.getItem().getId(),
                        cart.getItem().getName(),
                        cart.getItem().getPrice(),
                        cart.getItem().getDescription(),
                        cart.getItem().getImage(),
                        cart.getQuantity())
                )
        );

        return items;

    }

//    public List<Items> deleteItemFromCart(Users users, Items items){
//        if count becomes zero, delete entry
//        count should not be > item_availale_count


//    }
    public List<Items> deleteItemsFromCart(Users users){
        cartRepository.deleteAll();
        return new ArrayList<>();
    }
}
