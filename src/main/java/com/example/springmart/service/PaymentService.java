package com.example.springmart.service;

public class PaymentService {
    private static final String STRIPE_API_KEY = "sk_test_4eC39HqLyjWDarjtT1zdp7dc";

    public void processPayment() {
        // Use the API key directly in code
        Stripe.apiKey = STRIPE_API_KEY;
        // Payment processing logic...
    }
}
