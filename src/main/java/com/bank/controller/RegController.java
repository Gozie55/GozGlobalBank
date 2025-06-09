package com.bank.controller;

import com.bank.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bank.dao.CustomerRepository;
import com.bank.service.EmailService;
import com.bank.service.OtpService;

import com.bank.service.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author user
 */
@Controller
@RequestMapping("/api")
public class RegController {

    @Autowired
    CustomerRepository repo;

    final OtpService otpservice;
    final EmailService emailservice;
    final UserService userservice;

    public RegController(OtpService otpservice, EmailService emailService, UserService userservice) {
        this.otpservice = otpservice;
        this.emailservice = emailService;
        this.userservice = userservice;
    }

    @GetMapping("/register")
    public String login() {
        return "register";
    }

    @GetMapping("/otp")
    public String otp() {
        return ("otp");
    }

    @GetMapping("/pin")
    public String pin() {
        return ("pin");
    }

    @PostMapping("/confirmPin")
    public String method(@RequestParam int cpin) {
        Optional<Customer> emailOpt = repo.findByEmail(email);

        Customer updateEmail = emailOpt.get();
        updateEmail.setPin(cpin);

        repo.save(updateEmail);

        return "login";
    }

    String email;
    Customer cust;

    @PostMapping("/regPerson")
    public String RegController(Customer cust,
            String email) {

        this.email = email;
        this.cust = cust;

        String response = userservice.checkEmail(email);

        if (response.equals("Email Already Exists")) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response).toString();
            return "userExist";
        }

        String otp = otpservice.generateOtp(email);
        emailservice.sendOtpEmail(email, otp);
        return "otp";
    }

    @PostMapping("/validate")
    public String validateOtp(String otp) {
        boolean isValid = otpservice.validate(email, otp);
        if (isValid) {
// Ensure phone number is valid before processing
            String phone = String.valueOf(cust.getPhone());
            if (phone != null && phone.startsWith("0")) {
                // Remove leading zero
                String accountNumber = phone.substring(1);
                cust.setAccnumber(accountNumber);
            } else {
                // If no leading zero, just store as is
                cust.setAccnumber(phone);
            }
            userservice.registerUser(cust);
            return "pin";
        } else {
            return "error";
        }
    }
}
