package com.bank.controller;

import com.bank.dao.CustomerRepository;
import com.bank.entity.Customer;
import org.springframework.web.bind.annotation.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/payment")
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
            // Create charge
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount * 100); // Convert to cents
            chargeParams.put("currency", "usd");
            chargeParams.put("source", token);
            chargeParams.put("description", "Banking App Payment");

            Charge charge = Charge.create(chargeParams);

            response.put("status", "success");
            response.put("transactionId", charge.getId());

            // ✅ Update User's Balance in Database
            Object userObj = session.getAttribute("user");
            Customer customer = repo.findByUsername(userObj.toString());

            customer.setBalance(customer.getBalance() + amount);
            repo.save(customer);

            // ✅ Store updated balance in session for real-time update
            session.setAttribute("balance", customer.getBalance());

            response.put("newBalance", customer.getBalance()); // Send updated balance to frontend

        } catch (StripeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    // ✅ Update the user balance in database (Replace with actual DB update logic)
    private void updateUserBalance(int amount) {
        Object userObj = session.getAttribute("user");
        Customer customer = repo.findByUsername(userObj.toString());

        // Update user's balance
        customer.setBalance(customer.getBalance() + amount);
        repo.save(customer);
    }

    @GetMapping("/stripe-key")
    public Map<String, String> getStripeKey() {
        return Map.of("publishableKey", stripePublishableKey);
    }

    @GetMapping("/balance")
    public Map<String, Object> getBalance() {
        Object userObj = session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (userObj != null) {
            Customer customer = repo.findByUsername(userObj.toString());
            response.put("balance", customer.getBalance());
        } else {
            response.put("balance", 0);
        }

        return response;
    }

}
