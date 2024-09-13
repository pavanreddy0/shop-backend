package com.ShopSpring.ShopSpring.app.controller;

import com.ShopSpring.ShopSpring.app.dto.LoginResponse;
import com.ShopSpring.ShopSpring.app.dto.LoginUserDto;
import com.ShopSpring.ShopSpring.app.dto.RegisterUserDto;
import com.ShopSpring.ShopSpring.app.model.Users;
import com.ShopSpring.ShopSpring.app.service.AuthenticationService;
import com.ShopSpring.ShopSpring.app.utility.ApiResponse;
import com.ShopSpring.ShopSpring.app.utility.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            System.out.println("registerUserDto " + registerUserDto);
            authenticationService.signup(registerUserDto);

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            Users authenticatedUser = authenticationService.authenticate(loginUserDto);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getJwtExpiration());

            ApiResponse<LoginResponse> response = new ApiResponse<>("Success", HttpStatus.OK, loginResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);


        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
