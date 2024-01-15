package com.example.demo.Subscription;

import com.example.demo.User.User;
import com.example.demo.User.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class UserSubscriptionService {

    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final UserSubscriptionRepo userSubscriptionRepo;
    @Transactional
    public boolean checkSubscriptionGetDuration(String userId){
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        if(user.isPresent()) {
            LocalDate currentDate = LocalDate.now();
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth()); //the last day of the current month based on the current date.

            if (currentDate.isAfter(lastDayOfMonth.minusWeeks(1)) && currentDate.isBefore(lastDayOfMonth.plusWeeks(1))) {
                return !user.get().getStudentSubscription().isNextMonthSubscription();

            }
        }
            return false;
    }

    public boolean studentPurchaseSubscription(String userId){
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        LocalDate purchaseDate;
        LocalDate expiredDate;
        if(user.isPresent()){
            LocalDate currentDate = LocalDate.now();
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth()); //the last day of the current month based on the current date.

            if(currentDate.isAfter(lastDayOfMonth.minusWeeks(1)) && currentDate.isBefore(lastDayOfMonth.plusWeeks(1))){
                    if(currentDate.isAfter(lastDayOfMonth.minusWeeks(1))){
                            purchaseDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                            expiredDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                            if(user.get().getStudentSubscription().getId() == null){
                                var userSubscription = UserSubscription.builder()
                                        .nextMonthSubscription(true)
                                        .startDate(purchaseDate)
                                        .endDate(expiredDate)
                                        .build();
                                var userSave = User.builder()
                                        .studentSubscription(userSubscription)
                                        .build();
                                userRepo.save(userSave);
                            }else{
                                user.ifPresent(userSave ->{
                                   userSave.getStudentSubscription().setNextMonthSubscription(true);
                                   userSave.getStudentSubscription().setStartDate(purchaseDate);
                                   userSave.getStudentSubscription().setEndDate(expiredDate);
                                    userRepo.save(userSave);
                                });
                            }
                        return true;

                    } else if (currentDate.isBefore(lastDayOfMonth.plusWeeks(1))) {
                        purchaseDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                        expiredDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                        if(user.get().getStudentSubscription().getId() == null){
                            var userSubscription = UserSubscription.builder()
                                    .nextMonthSubscription(false)
                                    .startDate(purchaseDate)
                                    .endDate(expiredDate)
                                    .build();
                            User userSave = user.get();
                            userSave.setVerified(true);
                            userSave.setStudentSubscription(userSubscription);
                            userRepo.save(userSave);

                        }else{
                            user.ifPresent(userSave ->{
                                userSave.getStudentSubscription().setNextMonthSubscription(false);
                                userSave.getStudentSubscription().setStartDate(purchaseDate);
                                userSave.getStudentSubscription().setEndDate(expiredDate);
                                userSave.setVerified(true);
                                userRepo.save(userSave);
                            });
                        }
                        return true;
                    }
            }

        }else{
                return false;
        }

        return false;
    }

    public UserRespond getStudentTicketData(String userId){
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        String expiredDate;
        String purchaseDate;
        int daysDifference;

        if(user.isPresent()){

            boolean checkTimeLine = checkSubscriptionGetDuration(userId);

            if(user.get().getStudentSubscription() == null){

                    expiredDate = "0000/00/00";

                    purchaseDate = "0000/00/00";

                    daysDifference = 0;

            }else{
                if(user.get().getStudentSubscription().getEndDate() == null){
                    expiredDate = "0000/00/00";
                }else{
                    expiredDate = user.get().getStudentSubscription().getEndDate().toString();
                }
                if( user.get().getStudentSubscription().getStartDate() == null){
                    purchaseDate = "0000/00/00";
                }else{
                    purchaseDate = user.get().getStudentSubscription().getStartDate().toString();
                }

                if( user.get().getStudentSubscription().getStartDate() == null || user.get().getStudentSubscription().getEndDate() == null ){
                    daysDifference = 0;
                }else{
                    daysDifference = (int) ChronoUnit.DAYS.between(user.get().getStudentSubscription().getStartDate(), user.get().getStudentSubscription().getEndDate() );
                }
            }
            return UserRespond.builder()
                    .availableDays(daysDifference)
                    .expiredDate( expiredDate)
                    .purchaseDate( purchaseDate )
                    .verification(user.get().getVerified())
                    .purchaseAvailability(checkTimeLine)
                    .build();
        }
        return null;

    }
}
