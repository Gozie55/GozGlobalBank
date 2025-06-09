package com.bank.controller;

import com.bank.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.bank.dao.CustomerRepository;
import com.bank.service.EmailService;
import com.bank.service.OtpService;
import com.bank.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/api")
public class RegController {

    @Autowired
    CustomerRepository repo;

    final OtpService otpservice;
    final EmailService emailservice;
    final UserService userservice;

    // Hold temporary registration values
    private String email;
    private Customer cust;

    public RegController(OtpService otpservice, EmailService emailService, UserService userservice) {
        this.otpservice = otpservice;
        this.emailservice = emailService;
        this.userservice = userservice;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // ✅ maps to /pages/register.jsp
    }

    @GetMapping("/otp")
    public String otpPage() {
        return "otp"; // ✅ /pages/otp.jsp
    }

    @GetMapping("/pin")
    public String pinPage() {
        return "pin"; // ✅ /pages/pin.jsp
    }

    @PostMapping("/confirmPin")
    public String confirmPin(@RequestParam int cpin) {
        Optional<Customer> emailOpt = repo.findByEmail(email);
        if (emailOpt.isPresent()) {
            Customer updateEmail = emailOpt.get();
            updateEmail.setPin(cpin);
            repo.save(updateEmail);
            return "login"; // ✅ redirect to login page
        } else {
            return "error"; // ✅ fallback
        }
    }

    @PostMapping("/regPerson")
    public String registerCustomer(Customer cust, @RequestParam String email) {
        this.email = email;
        this.cust = cust;

        String response = userservice.checkEmail(email);
        if (response.equals("Email Already Exists")) {
            return "userExist"; // ✅ return to user exists page
        }

        String otp = otpservice.generateOtp(email);
        emailservice.sendOtpEmail(email, otp);
        return "otp"; // ✅ show OTP page
    }

    @PostMapping("/validate")
    public String validateOtp(@RequestParam String otp) {
        boolean isValid = otpservice.validate(email, otp);
        if (isValid) {
            String phone = String.valueOf(cust.getPhone());
            if (phone != null && phone.startsWith("0")) {
                String accountNumber = phone.substring(1);
                cust.setAccnumber(accountNumber);
            } else {
                cust.setAccnumber(phone);
            }
            userservice.registerUser(cust);
            return "pin"; // ✅ go to set pin page
        } else {
            return "error"; // ✅ OTP invalid
        }
    }
}
