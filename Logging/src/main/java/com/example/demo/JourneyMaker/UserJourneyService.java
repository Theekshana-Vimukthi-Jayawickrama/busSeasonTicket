package com.example.demo.JourneyMaker;

import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserJourneyService {
    @Autowired
    private UserJourneyRepository userJourneyRepository;

    @Autowired
    private UserRepo userRepo;

    public String updateUserJourney(LocalDate date, boolean hasJourney, String email) {
        try {
            Optional<User> userOptional = userRepo.findByEmail(email);

            if (userOptional.isPresent()) {
                if(userOptional.get().getVerified()){
                    User user = userOptional.get();
                    UUID userId = user.getId();
                    String userEmail = user.getEmail();

                    Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, date);

                    if (userJourneyOptional.isPresent()) {
                        UserJourney userJourney = userJourneyOptional.get();
                        userJourney.setJourneyCount(2);
                        userJourneyRepository.save(userJourney);
                        return "updated";
                    } else {
                        UserJourney userJourney = UserJourney.builder()
                                .user(user)
                                .email(userEmail)
                                .userId(userId)
                                .journeyDate(date)
                                .journeyCount(hasJourney ? 1 : 0)
                                .build();

                        user.getJourneys().add(userJourney);
                        userRepo.save(user);
                        return "updated";

                    }
                }else{
                    return ("Not verified");
                }

            } else {
                return ("Not present" );
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
            return "error";
    }

    public int attendance(UUID userId,LocalDate date){

        Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, date);

        if(userJourneyOptional.isPresent()){
            if(userJourneyOptional.get().getJourneyCount() == 1){
                return 1;
            }else if(userJourneyOptional.get().getJourneyCount() == 2) {
                return 2;
            } else if (userJourneyOptional.get().getJourneyCount() == 0) {
                return 0;
            }
        }
           return 404;
    }
}

