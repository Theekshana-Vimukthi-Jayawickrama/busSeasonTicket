package com.example.demo.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private double amount;
    private String stripeToken; // Token generated by Stripe Elements or Checkout
    private UUID userId;
}
