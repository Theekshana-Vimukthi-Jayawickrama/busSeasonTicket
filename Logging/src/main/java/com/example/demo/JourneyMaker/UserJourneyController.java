package com.example.demo.JourneyMaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/journey")
public class UserJourneyController {
    @Autowired
    private UserJourneyService userJourneyService;

    @PostMapping("/student/update")
    public ResponseEntity<String> updateUserJourney(@RequestBody UserJourneyRequest request) {
        try{
            LocalDate date =LocalDate.now();
            String status = userJourneyService.updateStudentJourney(date, request.isHasJourney(), request.getEmail());
            return ResponseEntity.ok(status);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/adult/update")
    public ResponseEntity<String> updateAdultUserJourney(@RequestBody UserJourneyRequest request) {
        try{
            LocalDate date =LocalDate.now();
            String status = userJourneyService.checkDays(date, request.isHasJourney(), request.getEmail());
            if(Objects.equals(status, "Updated")){
                return ResponseEntity.ok("Updated");
            }else if(Objects.equals(status, "Not verified")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not verified");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/student/checkJourney/{userId}/{date}")
    public ResponseEntity<Integer> checkJourney(@PathVariable UUID userId,@PathVariable LocalDate date){
//        UUID userId = UUID.fromString(id);
        try{
            int status = userJourneyService.markAttendanceStudents(userId,date);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/adult/daySelection/{userId}")
    public ResponseEntity<RouteDaysSelectionResponse> checkDaysThatSelected(@PathVariable UUID userId){
//        UUID userId = UUID.fromString(id);
        try{
            RouteDaysSelectionResponse routeDaysSelectionResponse = userJourneyService.getSelectedDaysByAdult(userId);
            return ResponseEntity.ok(routeDaysSelectionResponse);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


}

