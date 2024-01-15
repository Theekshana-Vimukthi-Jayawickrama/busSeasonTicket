package com.example.demo.Subscription;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/subscription")
public class UserSubscriptionController {

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @GetMapping("/getTicketDetails/{userId}")
    public ResponseEntity<UserRespond> getTicketData(@PathVariable String userId) {
        UserRespond studentRespond = userSubscriptionService.getStudentTicketData(userId);
        return ResponseEntity.ok(studentRespond);
    }

    @PostMapping("/setSubscription")
    public ResponseEntity<?> setSubscription(@RequestBody UserSetSubsRequest userSetSubsRequest) {

          boolean status = userSubscriptionService.studentPurchaseSubscription(userSetSubsRequest.getUserId());
          if(status){
              return ResponseEntity.ok("Successfully subscribe");
          }else{
              return ResponseEntity.ofNullable("unsuccessful");
          }
    }

}
