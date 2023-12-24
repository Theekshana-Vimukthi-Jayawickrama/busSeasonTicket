package com.example.demo.auth;

import com.example.demo.user.ApprovalLetter;
import com.example.demo.user.ApprovalLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @Autowired
    private ApprovalLetterRepository pdfDocumentRepository;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable Integer id) {
        ApprovalLetter pdfDocument = pdfDocumentRepository.findById(id)
                .orElse(null);
        if (pdfDocument != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + pdfDocument.getName())
                    .body(pdfDocument.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart("request") RegisterRequest request,
            @RequestPart("schoolDetailRequest") SchoolRequest schoolRequest,
            @RequestPart("guardianDetailsRequest") GuardianRequest guardianRequest,
            @RequestPart("routeDetails") RouteRequest routeRequest,
            @RequestParam(value = "approvalLetter", required = false) MultipartFile approvalLetter,
            @RequestParam(value = "birthFile", required = false) MultipartFile birthFile
    ) {
        try {
            AuthenticationResponse response = service.register(request,schoolRequest,guardianRequest,routeRequest,birthFile,approvalLetter);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOTP(
            @RequestParam("email") String email
    ) {
        try {
            service.resendOTP(email);
            return ResponseEntity.ok("OTP sent successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User with provided email doesn't exist");
        }

    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<String> verifyOTP(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp
    ) {
        try {
            String status = service.verifyOTP(email, otp);
            if (Objects.equals(status, "OTP verification successful.")) {
                return ResponseEntity.ok(status);
            } else if (Objects.equals(status, "OTP is expired")) {
                return ResponseEntity.badRequest().body("OTP is expired");
            } else if (Objects.equals(status, "Invalid OTP")) {
                return ResponseEntity.badRequest().body("Invalid OTP");
            } else if (Objects.equals(status, "Email does not exit")) {
                return ResponseEntity.badRequest().body("Email does not exit");
            }else{
                return ResponseEntity.badRequest().body("verification unsuccessful");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }
}
