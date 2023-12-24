package com.example.demo.smsOTP;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/smsOTP")
public class SmsOTPController {

    private final SmsOTPService service;

    public SmsOTPController(SmsOTPService service) {
        this.service = service;
    }

    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOTP(@RequestParam("phoneNumber") String phoneNumber,@RequestParam("email") String email) {
        try{
            service.generateAndSendOTP(phoneNumber, email);
            return ResponseEntity.ok("OTP generated and saved successfully!");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to OTP: " + e.getMessage());
        }


    }
}
