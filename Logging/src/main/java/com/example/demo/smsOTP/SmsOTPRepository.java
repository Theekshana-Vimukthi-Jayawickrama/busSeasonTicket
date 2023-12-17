package com.example.demo.smsOTP;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsOTPRepository extends JpaRepository<SmsOTP,Integer> {
    Optional<SmsOTP> findByCodeAndExpirationTimeAfter(String code, LocalDateTime currentTime);
}
