package com.example.demo.Payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
class PaymentController {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostMapping("/charge")
    public String chargeCard(@RequestBody ChargeRequest chargeRequest) throws StripeException, StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", chargeRequest.getAmount()); // amount in cents, e.g., $10.00 = 1000
        params.put("currency", "usd");
        params.put("source", chargeRequest.getToken()); // token obtained from Stripe Elements or Checkout

        Charge charge = Charge.create(params);

        // Handle successful charge
        return "Payment successful!";
    }
}