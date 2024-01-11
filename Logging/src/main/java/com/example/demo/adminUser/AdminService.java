package com.example.demo.adminUser;

import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;


@Service
@CrossOrigin
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private final DistrictListRepository districtListRepository;
    private final UserRepo userRepo;
    private final JavaMailSender emailSender;


    public boolean saveDistrict(DistrictRequest districtSch) {

        String district = districtSch.getDistrictName().toLowerCase().trim();
        Optional<DistrictList> schoolDistrict = districtListRepository.findByDistrictName(district);
        if (schoolDistrict.isPresent()) {
            return false;
        } else {

            DistrictList districtList = DistrictList.builder()
                    .districtName(district)
                    .build();
            districtListRepository.save(districtList);
            return true;
        }
    }

    public List<String> getAllSchoolDistrict() {

        List<DistrictList> schoolDistrict = districtListRepository.findAll();
        if (schoolDistrict.isEmpty()) {
            return null;
        } else {
            List<String> districts = new ArrayList<>();

            for (int i = 0; i < schoolDistrict.size(); i++) {
                districts.add(schoolDistrict.get(i).getDistrictName().toUpperCase());
            }
            return districts;
        }
    }

    public List<PendingUserResponse> getPendingUsers() {

        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            return null;
        } else {
            List<PendingUserResponse> userName = new ArrayList<PendingUserResponse>();

            for (User user : users) {
                if (Objects.equals(user.getStatus(), "pending".toLowerCase().trim())) {
                    PendingUserResponse pendingUser = PendingUserResponse.builder()
                            .userName(user.getFullname())
                            .id(user.getId())
                            .status(user.getStatus())
                            .build();
                    userName.add(pendingUser);
                }
            }
            return userName;
        }
    }

    public boolean userStatusUpdate(UUID userId, UserUpdateRequest userUpdateRequest) {

        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(userUpdateRequest.getStatus().trim().toLowerCase());
            // Send approval email logic can go here

            if(userUpdateRequest.getStatus().toLowerCase().trim().equals("pending".toLowerCase().trim())){
                SimpleMailMessage message = new SimpleMailMessage();
                String toEmail = user.getEmail();
                message.setTo(toEmail);
                message.setSubject("Congratulations" + user.getFullname() + "!");
                message.setText("You're eligible for our service. You can now log into your user account and make a payment to get the season ticket.");
                emailSender.send(message);
                userRepo.save(user);
                return true;
            }else{
                SimpleMailMessage message = new SimpleMailMessage();
                String toEmail = user.getEmail();
                message.setTo(toEmail);
                message.setSubject( user.getFullname() + ",I regret to share that, based on the credentials provided.");
                message.setText("\n" +
                        "Apologies, it seems that the credentials provided aren't meeting the eligibility criteria for our service. Therefore, we're unable to accept them for accessing the user account or making the payment for the season ticket. Please try again with different credentials. It's possible that the ones provided don't meet the requirements for accessing our service.");
                emailSender.send(message);
                userRepo.save(user);
                return true;
            }

        }else{
            return false;
        }

    }


}
