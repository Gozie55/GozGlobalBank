/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.service;

import com.bank.dao.CustomerRepository;
import com.bank.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author CHIGOZIE IWUJI
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    CustomerRepository repo;
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JWTService jwtservice;

    public UserService(CustomerRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(Customer cust) {
        String encryptedPassword = passwordEncoder.encode(cust.getPassword());
        cust.setPassword(encryptedPassword);
        repo.save(cust);
        return "Successful";
    }

    public String checkEmail(String email) {
        if (repo.existsByEmail(email)) {
            return "Email Already Exists";
        } else {
            return "Email Available";
        }
    }

    public String checkUsername(String username) {
        if (repo.existsByUsername(username)) {
            return "Username Already Exists";
        } else {
            return "Username Available";
        }
    }

    public String checkPhone(long phone) {
        if (repo.existsByPhone(phone)) {
            return "Phone Already Exists";
        } else {
            return "Phone Available";
        }
    }

    public boolean validateUser(Customer cust) {
        Customer user = repo.findByUsername(cust.getUsername());

        if (user == null) {
            return false;
        }
//        checking if the password entered is same as what is in the database
        return passwordEncoder.matches(cust.getPassword(), user.getPassword());
    }

    public String verify(Customer cust) {

//        UserDetails user = (UserDetails) authentication.getPrincipal();
        try {
            // Authenticate user
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cust.getUsername(),
                            cust.getPassword()));

            if (authentication.isAuthenticated()) {
                // Fetch user from DB
                Customer user = repo.findByUsername(cust.getUsername());

                // Generate JWT token
                String token = jwtservice.generateToken(user);

                // Log the generated token
                logger.info("JWT Token generated for user {}: {}", user.getUsername(), token);

                return token;
            }
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", cust.getUsername(), e);
        }

        return "fail";
    }

    public Customer getUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    public String extractUsernameFromToken(String token) {
        return jwtservice.extractUserName(token);
    }

}
