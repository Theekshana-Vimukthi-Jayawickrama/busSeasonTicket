package com.example.demo.smsOTP;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSService {
    private final String ACCOUNT_SID = "AC78832ed086280fd8da56db9e51b093af";
    private final String AUTH_TOKEN = "e96e9faf278c94433e2fcb92847e63d1";
    private final String FROM_NUMBER = "+17732952247"; // Your Twilio phone number

    public TwilioSMSService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendOTP(String phoneNumber, String otp) {
        String message = "Your OTP is: " + otp;

        try {
            PhoneNumber to = new PhoneNumber(phoneNumber);
            PhoneNumber from = new PhoneNumber(FROM_NUMBER);
            MessageCreator creator = Message.creator(to,from,message);
            creator.create();
//            Message twilioMessage = Message.creator(
//                            new PhoneNumber(phoneNumber), // To
//                            new PhoneNumber(FROM_NUMBER), // From
//                            message)
//                    .create();
//            System.out.println("Twilio Message SID: " + twilioMessage.getSid());
        } catch (Exception e) {
            // Handle Twilio exceptions
            e.printStackTrace();
        }
    }
}

