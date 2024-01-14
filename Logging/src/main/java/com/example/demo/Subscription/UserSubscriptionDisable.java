package com.example.demo.Subscription;

import com.example.demo.Student.User;
import com.example.demo.Student.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSubscriptionDisable {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserSubscriptionRepo studentSubscriptionRepo;

    @Scheduled(cron = "0 0 0 L * ?") // Execute at midnight on the last day of the month
//    @Scheduled(fixedRate = 1000)
    public void executeMonthlyTask() {
        List<User> users = userRepo.findAll();
        for (User user : users) {

                if ( user.getStudentSubscription().isNextMonthSubscription()) {
                        user.setVerified(true);
                        userRepo.save(user);
                } else {
                    user.setVerified(false);
                    userRepo.save(user);
                }
        }
    }
}
