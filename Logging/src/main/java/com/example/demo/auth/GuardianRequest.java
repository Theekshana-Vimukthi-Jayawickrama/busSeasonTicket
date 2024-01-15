package com.example.demo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuardianRequest {
    private String nameOfGuardian;
    private String guardianType;
    private String occupation;
    private String contactNumber;
}
