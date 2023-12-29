package com.example.demo.Payment;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment/")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private PaymentService service;

    @Autowired
    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ... previous code remains unchanged ...
    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostMapping("/charge")
    public ResponseEntity<String> chargeCard(@RequestBody PaymentRequest paymentRequest) {


        Stripe.apiKey = secretKey;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", paymentRequest.getAmount());
            params.put("currency", "usd");
            params.put("source", paymentRequest.getStripeToken());

            Charge charge = Charge.create(params);

            if (charge.getPaid()) { // Check if the payment was successful
//                service.saveSuccessfulPayment()
                Payment payment = new Payment();
                // Additional fields or operations for the Payment entity can be added here
                paymentRepository.save(payment); // Save the payment to your MySQL database
                return ResponseEntity.ok("Payment successful");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + e.getMessage());
        }
    }
}


