package com.example.demo.JourneyMaker;

import com.example.demo.User.Role;
import com.example.demo.User.User;
import com.example.demo.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DailyJourneyUpdater {

    @Autowired
    private UserJourneyService userJourneyService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private UserJourneyRepository userJourneyRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
//    @Scheduled(fixedRate = 1000)
    public void updateJourneyCounts() {
        // Get yesterday's date
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // Get all users (You might have a different method to retrieve users)
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // Check if the user had a journey yesterday
            if(user.getVerified().equals(true)){
                boolean hasJourneyYesterday = checkJourneyForUserYesterday(user.getId(), yesterday);

                if(!hasJourneyYesterday){
                    if(user.getRole() == Role.STUDENT){
                        String email = user.getEmail();
                        userJourneyService.updateStudentJourney( yesterday, false, email);
                    }else if(user.getRole() == Role.ADULT){
                        String email = user.getEmail();
                        userJourneyService.checkDays( yesterday, false, email);
                    }
                }
            }
            // Update the journey count for the user based on yesterday's journey

        }
    }

    private boolean checkJourneyForUserYesterday(UUID userId, LocalDate yesterday) {
        // Query the database using UserJourneyRepository to check if there's a journey for the user on yesterday's date
        Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, yesterday);

        return userJourneyOptional.isPresent(); // Return true if a journey exists for the user on the specified date
    }
}

