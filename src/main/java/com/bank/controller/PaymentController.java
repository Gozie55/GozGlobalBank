package com.bank.controller;

import com.bank.dao.CustomerRepository;
import com.bank.entity.Customer;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    CustomerRepository repo;

    @Autowired
    HttpSession session;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.publishable.key}")
    private String stripePublishableKey;

    @PostMapping("/charge")
    public Map<String, Object> charge(@RequestParam String token, @RequestParam int amount) {
        Stripe.apiKey = stripeSecretKey;
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount * 100);
            chargeParams.put("currency", "usd");
            chargeParams.put("source", token);
            chargeParams.put("description", "Banking App Payment");

            Charge charge = Charge.create(chargeParams);

            response.put("status", "success");
            response.put("transactionId", charge.getId());

            Object userObj = session.getAttribute("user");
            Customer customer = repo.findByUsername(userObj.toString());

            customer.setBalance(customer.getBalance() + amount);
            repo.save(customer);
            session.setAttribute("balance", customer.getBalance());

            response.put("newBalance", customer.getBalance());

        } catch (StripeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/stripe-key")
    public Map<String, String> getStripeKey() {
        return Map.of("publishableKey", stripePublishableKey);
    }

    @GetMapping("/balance")
    public Map<String, Object> getBalance() {
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            return Map.of("status", "error", "message", "User not logged in", "balance", 0);
        }

        Customer customer = repo.findByUsername(userObj.toString());
        if (customer == null) {
            return Map.of("status", "error", "message", "Customer not found", "balance", 0);
        }

        return Map.of("status", "success", "balance", customer.getBalance());
    }
}
