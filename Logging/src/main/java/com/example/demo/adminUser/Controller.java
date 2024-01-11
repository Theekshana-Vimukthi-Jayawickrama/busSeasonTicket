package com.example.demo.adminUser;

import com.example.demo.QRcode.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private final AdminService adminService;
    @Autowired
    private QRCodeService qrCodeService;


    @GetMapping("/getAllPendingUsers")
    public ResponseEntity<List<PendingUserResponse>> getUsersByStatus() {
        try {
                return ResponseEntity.ok(adminService.getPendingUsers());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/setUserStatus/generateQRCode/{userId}")
    public ResponseEntity<?> approveUser(@PathVariable UUID userId,@RequestBody UserUpdateRequest userUpdateRequest) {
        try {
                if(adminService.userStatusUpdate(userId,userUpdateRequest)){
                    if(qrCodeService.saveQRCode(userId)){
                        return ResponseEntity.ok("QR Code generated and saved successfully!, & status updated");
                    }else{
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("status updated but Failed to generate QR Code: ");
                    }
                }else{
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("User not found.");
                }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addSchoolDistrict")
    public ResponseEntity<?> setSchoolDistrict(@RequestBody DistrictRequest districtName) {

        boolean status = adminService.saveDistrict(districtName);
        try {
            if (status) {
                return ResponseEntity.ok("Saved");
            } else {
                return ResponseEntity.badRequest().body("Already added");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllSchDistrict")
    public ResponseEntity<List<String>> getAllSchoolDistricts (){

        try {
            List<String> districts = adminService.getAllSchoolDistrict();

            if(districts == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(districts);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
