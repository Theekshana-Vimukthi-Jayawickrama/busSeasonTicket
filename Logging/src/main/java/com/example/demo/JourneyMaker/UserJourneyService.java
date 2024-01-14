package com.example.demo.JourneyMaker;

import com.example.demo.Student.User;
import com.example.demo.Student.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import java.time.DayOfWeek;


@Service
@Transactional
public class UserJourneyService {
    @Autowired
    private UserJourneyRepository userJourneyRepository;

    @Autowired
    private UserRepo userRepo;

    public String checkDays(LocalDate date, boolean hasJourney, String email){
        try {
            Optional<User> userOptional = userRepo.findByEmail(email);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (userOptional.isPresent()) {

                switch (dayOfWeek) {
                    case MONDAY:
                        if(userOptional.get().getSelectDays().isMonday()){
                            return updateStudentJourney(date,hasJourney,email);

                    }else{
                            return "day not available for user";
                        }
                    case TUESDAY:
                        if(userOptional.get().getSelectDays().isTuesday()){
                            return   updateStudentJourney(date,hasJourney,email);

                        }else{
                            return "day not available for user";
                        }
                    case WEDNESDAY:
                        if(userOptional.get().getSelectDays().isWednesday()){
                            return  updateStudentJourney(date,hasJourney,email);

                        }else{
                            return "day not available for user";
                        }
                    case THURSDAY:
                        if(userOptional.get().getSelectDays().isThursday()){
                            return  updateStudentJourney(date,hasJourney,email);

                        }else{
                            return "day not available for user";
                        }
                    case FRIDAY:
                        if(userOptional.get().getSelectDays().isFriday()){
                            return  updateStudentJourney(date,hasJourney,email);

                        }else{
                            return "day not available for user";
                        }
                    case SATURDAY:
                        if(userOptional.get().getSelectDays().isSaturday()){
                            return  updateStudentJourney(date,hasJourney,email);

                        }else{
                            return "day not available for user";
                        }
                    case SUNDAY:
                        if(userOptional.get().getSelectDays().isSunday()){
                            return  updateStudentJourney(date,hasJourney,email);

                        }else{
                            return "day not available for user";
                        }
                    default:
                        return "day not available.";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "There is no a user.";
    }

    public String updateStudentJourney(LocalDate date, boolean hasJourney, String email) {
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

    public int markAttendanceStudents(UUID userId, LocalDate date){

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

