package com.bank.controller;

import com.bank.entity.Customer;
import com.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserService userservice;

    public LoginController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/login")  // ✅ Removed "/api" prefix
    public ModelAndView login(Customer user, Model model, HttpServletRequest request) {
        // Authenticate user
        String token = userservice.verify(user);

        if ("fail".equals(token)) {
            model.addAttribute("error", "Invalid Credentials");
            return new ModelAndView("pages/login");  // ✅ Correct path to /WEB-INF/pages/login.jsp
        }

        // Fetch user from DB
        Customer authenticatedUser = userservice.getUserByUsername(user.getUsername());

        // Store session attributes
        HttpSession session = request.getSession();
        session.setAttribute("token", token);
        session.setAttribute("user", authenticatedUser.getUsername());
        session.setAttribute("balance", authenticatedUser.getBalance());

        // Pass values to JSP
        model.addAttribute("token", token);
        model.addAttribute("user", authenticatedUser);
        model.addAttribute("balance", authenticatedUser.getBalance());

        System.out.println("Generated JWT Token: " + token);

        // ✅ Correctly render /WEB-INF/pages/index.jsp
        return new ModelAndView("pages/index");
    }
}
