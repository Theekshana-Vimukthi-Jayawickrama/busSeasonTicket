package com.example.demo.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // the appropriate getters, setters, toString, equals, hashCode, and a constructor for all non-initialized final fields.
@Builder //allowing to chain method calls together and set individual fields with clear, readable code.
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRequest {
    private String token;
    private int amount;
    // getters and setters
}