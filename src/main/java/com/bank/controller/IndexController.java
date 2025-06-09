package com.bank.controller;

import com.bank.entity.Customer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class IndexController {

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/api/index";
    }

    @GetMapping("/home")
    public ModelAndView home(Customer person) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("obj", person);
        mv.setViewName("index");  // ✅ Point to pages/index.jsp
        return mv;
    }

    @GetMapping("/login")
    public String login() {
        return "login";  // ✅ pages/login.jsp
    }

    @GetMapping("/index")
    public String index() {
        return "pages/index";  // ✅ pages/index.jsp
    }

    @GetMapping("/fund")
    public String fund() {
        return "fund";  // ✅ pages/fund.jsp
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // You can set this to false locally
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/api/index";  // Redirects to pages/index.jsp via /index route
    }
}
