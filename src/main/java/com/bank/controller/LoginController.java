/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.controller;


import com.bank.entity.Customer;
import com.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author CHIGOZIE IWUJI
 */
@Controller
@RequestMapping("/api")
public class LoginController {

    @Autowired
    UserService userservice;

    public LoginController(UserService userservice) {
        this.userservice = userservice;

    }

    @PostMapping("/login")
    public ModelAndView login(Customer user, Model model, HttpServletRequest request) {
        // Authenticate user
        String token = userservice.verify(user);

        if ("fail".equals(token)) {
            model.addAttribute("error", "Invalid Credentials");
            return new ModelAndView("error"); // Show login.jsp on failure
        }

        // Fetch user from DB
        Customer authenticatedUser = userservice.getUserByUsername(user.getUsername());

        // Add attributes for JSP
        model.addAttribute("token", token);

        HttpSession session = request.getSession();
        session.setAttribute("token", token);
        session.setAttribute("user", authenticatedUser.getUsername());
        session.setAttribute("balance", authenticatedUser.getBalance());

        // Log token for debugging
        System.out.println("Generated JWT Token: " + token);

        // Return index.jsp page
        return new ModelAndView("index");
    }
}
