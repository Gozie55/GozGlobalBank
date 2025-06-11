package com.bank.controller;

import com.bank.entity.Customer;
import com.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private UserService userservice;

    public LoginController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer user, HttpServletRequest request) {
        String token = userservice.verify(user);

        if ("fail".equals(token)) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }

        Customer authenticatedUser = userservice.getUserByUsername(user.getUsername());

        HttpSession session = request.getSession();
        session.setAttribute("token", token);
        session.setAttribute("user", authenticatedUser.getUsername());
        session.setAttribute("balance", authenticatedUser.getBalance());

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token,
                "user", authenticatedUser
        ));
    }
}
