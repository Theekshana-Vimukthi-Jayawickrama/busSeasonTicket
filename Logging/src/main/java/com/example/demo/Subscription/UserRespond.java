package com.example.demo.Subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRespond {
    private String purchaseDate;
    private  String expiredDate;
    private Integer availableDays;
    private boolean verification;
    private boolean purchaseAvailability;
}
