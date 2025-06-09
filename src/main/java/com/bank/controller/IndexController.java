package com.bank.controller;

import com.bank.entity.Customer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping("/")
    public String root() {
        return "pages/index";  // ✅ maps to /WEB-INF/pages/index.jsp
    }

    @GetMapping("/home")
    public ModelAndView home(Customer person) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("obj", person);
        mv.setViewName("pages/index");  // ✅ maps to /WEB-INF/pages/index.jsp
        return mv;
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/fund")
    public String fund() {
        return "pages/fund";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to false locally if needed
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
