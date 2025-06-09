/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bank.controller;

import com.bank.entity.Customer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author CHIGOZIE IWUJI
 */
@Controller
@RequestMapping("api/")
public class IndexController {

    @GetMapping("/home")
    public ModelAndView home(Customer person) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("obj", person);
        mv.setViewName("index");
        return mv;
    }

    @GetMapping("/login")
    public String login() {
        return ("login");
    }

    @GetMapping("/index")
    public String index() {
        return ("index");
    }

    @GetMapping("/fund")
    public String fund() {
        return ("fund");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the session
        request.getSession().invalidate();

        // Remove the JWT token cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to false for local development if needed
        cookie.setPath("/");
        cookie.setMaxAge(0); // Delete the cookie

        response.addCookie(cookie);

        return "redirect:/api/index"; // Redirect to home page
    }
}
