package com.ShopSpring.ShopSpring.app.service;

import com.ShopSpring.ShopSpring.app.dto.LoginUserDto;
import com.ShopSpring.ShopSpring.app.dto.RegisterUserDto;
import com.ShopSpring.ShopSpring.app.model.Users;
import com.ShopSpring.ShopSpring.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Users signup(RegisterUserDto input) {
        Users user = new Users();
        user.setName(input.getName());
        user.setUsername(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setSeller(input.isSeller());
        System.out.println("input " + input);
        System.out.println("user " + user);

        return userRepository.save(user);
    }

    public Users authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getEmail())
                .orElseThrow();
    }
}
