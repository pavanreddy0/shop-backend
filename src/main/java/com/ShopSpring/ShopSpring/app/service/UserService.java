package com.ShopSpring.ShopSpring.app.service;

import com.ShopSpring.ShopSpring.app.model.Users;
import com.ShopSpring.ShopSpring.app.repository.CartRepository;
import com.ShopSpring.ShopSpring.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService  {
    @Autowired
    private UserRepository userReposiroty;

    @Autowired
    private CartRepository cartRepository;

    /**
     * Once user logs in,
     * I should be able to see some items on my page
     * Cart items
     * Orders in the Orders Page
     */
//    public List<Items> getItemsInCart(Users users){
//        return cartRepository.findAllByUser(users);
//    }
    public List<Users> getAllUsers(){
        return userReposiroty.findAll();
    }

    public Users getUserByMail(String email) throws UsernameNotFoundException{
        Optional<Users> users = userReposiroty.findByUsername(email);
        return users.get();
    }

    public List<Users> saveUser(Users users){
        userReposiroty.save(users);
        return getAllUsers();
    }


}
