package com.bank.controller;

import com.bank.entity.Customer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping
    public String index(HttpServletRequest request) {
        Customer user = (Customer) request.getSession().getAttribute("user");
        return user != null ? "Welcome " + user.getUsername() + ", balance: ₦" + user.getBalance()
                            : "Guest mode. Please log in.";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "Logged out successfully";
    }
}
