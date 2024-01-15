package com.example.demo.JourneyMaker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJourneyRequest {
    private String email;
    private boolean hasJourney;
}
