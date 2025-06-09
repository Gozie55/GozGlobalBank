package com.bank.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public String chargeCard(String token, double amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        ChargeCreateParams params = ChargeCreateParams.builder()
            .setAmount((long) (amount * 100)) // Convert to cents
            .setCurrency("usd")
            .setDescription("Payment to Your Bank App")
            .setSource(token) // The Stripe token from the frontend
            .build();

        Charge charge = Charge.create(params);
        return charge.getId();
    }
}