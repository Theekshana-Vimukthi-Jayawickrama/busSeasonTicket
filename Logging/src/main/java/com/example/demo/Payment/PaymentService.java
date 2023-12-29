package com.example.demo.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository successfulPaymentRepository;

    public void saveSuccessfulPayment(double amount, String chargeId, UUID userId) {
        // Logic to save successful payments to MySQL database
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setChargeId(chargeId);
        payment.setUserId(userId);
        // Set other necessary fields
        successfulPaymentRepository.save(payment);
    }
}

