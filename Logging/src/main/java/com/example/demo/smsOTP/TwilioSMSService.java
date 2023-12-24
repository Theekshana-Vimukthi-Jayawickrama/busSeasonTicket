package com.example.demo.smsOTP;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSService {
    private final String ACCOUNT_SID = "AC8be5a69eb1ef6c7be5d218419802e6dc";
    private final String AUTH_TOKEN = "bb26172eba4cdcb180431e99a099f5e4";
    private final String FROM_NUMBER = "+14129270907"; // Your Twilio phone number

    public TwilioSMSService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendOTP(String phoneNumber, String otp) {
        String message = "Your OTP is: " + otp;

        try {
            Message twilioMessage = Message.creator(
                            new PhoneNumber(phoneNumber), // To
                            new PhoneNumber(FROM_NUMBER), // From
                            message)
                    .create();
            System.out.println("Twilio Message SID: " + twilioMessage.getSid());
        } catch (Exception e) {
            // Handle Twilio exceptions
            e.printStackTrace();
        }
    }
}

