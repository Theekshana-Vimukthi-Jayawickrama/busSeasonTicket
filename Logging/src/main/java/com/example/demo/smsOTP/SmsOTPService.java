package com.example.demo.smsOTP;

import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SmsOTPService {
    private static final int EXPIRATION_MINUTES = 5; // Expiration time for OTP in minutes

    @Autowired
    private SmsOTPRepository otpRepository;

    @Autowired
    private UserRepo userRepository;

    private final TwilioSMSService twilioSMSService;

    public SmsOTPService(TwilioSMSService twilioSMSService) {
        this.twilioSMSService = twilioSMSService;
    }

    public void generateAndSendOTP(String phoneNumber, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String generatedOTP = generateOTP();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        saveOTP(user, generatedOTP, expirationTime);
        twilioSMSService.sendOTP(phoneNumber, generatedOTP); // Call SMS service to send OTP to the user's phone
    }

//    public boolean verifyOTP(String phoneNumber, String code) {
//        Optional<SmsOTP> otpOptional = otpRepository.findByCodeAndExpirationTimeAfter(code, LocalDateTime.now());
//
//        if (otpOptional.isPresent()) {
//            SmsOTP otp = otpOptional.get();
//
//            User user = otp.getUser();
//            if (user.getPhoneNumber().equals(phoneNumber)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public void saveOTP(User user, String code, LocalDateTime expirationTime) {
        SmsOTP otp = new SmsOTP();
        otp.setCode(code);
        otp.setExpirationTime(expirationTime);
        user.setSmsOtp(otp);
        userRepository.save(user);
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Appending random digits (0-9) to the OTP
        }

        return otp.toString();
    }



}
