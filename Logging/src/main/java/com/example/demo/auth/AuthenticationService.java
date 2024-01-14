package com.example.demo.auth;

import com.example.demo.OTPGenerator.OTP;
import com.example.demo.OTPGenerator.OTPRepository;
import com.example.demo.busRoutes.BusRoute;
import com.example.demo.busRoutes.BusRouteRepository;
import com.example.demo.config.JwtService;
import com.example.demo.demo.EmailAlreadyExistsException;
import com.example.demo.demo.UserService;
import com.example.demo.Student.*;
import com.example.demo.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepo repository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final StudentBirthFilesRepo uploadRepository;
    private final UserService userService;
    @Autowired
    private final StudentBirthFilesRepo studentBirthFilesRepo;
    @Autowired
    private final OTPRepository otpRepository;
    private final JavaMailSender emailSender;
    @Autowired
    private final BusRouteRepository busRouteRepository;
    @Autowired
    private final ApprovalLetterRepository approvalLetterRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request, SchoolRequest stuRequest, GuardianRequest guardianRequest, RouteRequest routeRequest, MultipartFile birthFile, MultipartFile approvalLetter, MultipartFile userPhoto) throws Exception {

        String email = request.getEmail();
        // Check if email already exists
        if (!userService.isEmailUnique(email)) {
            // Handle duplicate email error.
            throw new EmailAlreadyExistsException("Email already exists");
        }

        SchoolDetails schoolDetails = SchoolDetails.builder()
                .schAddress(stuRequest.getSchAddress())
                .district(stuRequest.getDistrict())
                .indexNumber(stuRequest.getIndexNumber())
                .build();

        GuardianDetails guardianDetails = GuardianDetails.builder()
                .nameOfGuardian(guardianRequest.getNameOfGuardian())
                .contactNumber(guardianRequest.getContactNumber())
                .occupation(guardianRequest.getOccupation())
                .guardianType(guardianRequest.getGuardianType())
                .build();

        String route = routeRequest.getRoute();
        String nearestDeport =routeRequest.getNearestDeport();
        Double charge = routeRequest.getCharge();
        StuBusDetails stuBusDetails = stuBusRoute(route,charge,nearestDeport);

       var user = User.builder()
               .fullName(request.getFullname())
                .intName(request.getIntName())
                .email(email)
                .stuBusDetails(stuBusDetails)
                .schoolDetails(schoolDetails)
                .guardianDetails(guardianDetails)
                .gender(request.getGender())
                .dob(request.getDob())
                .telephoneNumber(request.getTelephone())
                .residence(request.getResidence())
                .verified(false)
                .address(request.getAddress())
                .status("Pending".trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();

        //Approval file save
        ApprovalLetter stuApprovalLetter = stuApprovalLetter(approvalLetter);
        if(stuApprovalLetter==null){
            throw new Exception("File could not be saved");
        }else{
            user.setApprovalLetter(stuApprovalLetter);
        }
        //Student Birth File
        StudentBirthFiles studentBirthFiles = stuBirthFile(birthFile);
        if(studentBirthFiles==null){
            throw new Exception("File could not be saved");
        }else{
            user.setStudentBirthFiles(studentBirthFiles);
        }

        //photo
        StudentPhotos userPhoto1 = userPhotoUpload(userPhoto);
        if(userPhoto1==null){
            throw new Exception("File could not be saved");
        }else{
            user.setUserPhoto(userPhoto1);
        }

        repository.save(user);
        var jwtToken = JwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //Student PDF letter
    @Transactional
    public ApprovalLetter stuApprovalLetter(MultipartFile file){

        try {
            ApprovalLetter pdfDocument = new ApprovalLetter();
            pdfDocument.setName(file.getOriginalFilename());
            pdfDocument.setType(file.getContentType());
            pdfDocument.setData(file.getBytes());
            return pdfDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Student BirthFile
    @Transactional
    public StudentBirthFiles stuBirthFile(MultipartFile file){

        try {
            StudentBirthFiles pdfDocument = new StudentBirthFiles();
            pdfDocument.setFileBirthName(file.getOriginalFilename());
            pdfDocument.setFileBirthType(file.getContentType());
            pdfDocument.setFileBirthData(file.getBytes());
            return pdfDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
@Transactional
    public StudentPhotos userPhotoUpload (MultipartFile file){

        try {
            StudentPhotos photo = new StudentPhotos();
            photo.setUserPhotoName(file.getOriginalFilename());
            photo.setPhotoType(file.getContentType());
            photo.setData(file.getBytes());
            return photo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadImage(Long fileId){
        Optional<StudentBirthFiles> dbImageData = uploadRepository.findById(fileId);
        return ImageUtils.decompressImage(dbImageData.get().getFileBirthData());
    }

    //BusRoute that student will go...
    public StuBusDetails stuBusRoute(String route, Double charge,String nearestDeport){
        BusRoute busRoute = busRouteRepository.findByRoute(route);
        try {
            if (busRoute != null) {
                String distance = busRoute.getDistance();

                return StuBusDetails.builder()
                        .charge(charge)
                        .distance(distance)
                        .route(route)
                        .nearestDeport(nearestDeport)
                        .build();
            } else {
                // Handle case where busRoute is not found for the given route
                return null;
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during processing
            return null;
        }
    }


    //Email works and OTP generate...
        public boolean sendOTPEmail(String toEmail, Integer otp) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject("OTP Verification");
                message.setText("Your OTP for verification is: " + otp + ". This will expire within TWO minutes.");
                emailSender.send(message);
                return true; // Email sent successfully
            } catch (MailException e) {
                // Handle exceptions (e.g., log or perform appropriate actions)
                e.printStackTrace();
                return false; // Email sending failed
            }
        }
    public void sendOTP(String email)  {
        Optional<OTP> userOTP = otpRepository.findByEmail(email);

        if(userOTP.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = now.plusMinutes(2);
            Integer newOTP = generateOTP();
            OTP otp = userOTP.get();
            otp.setOtpCode(newOTP);
            otp.setOtpExpiryTime(expiryTime);
            otpRepository.save(otp);
            sendOTPEmail(email, newOTP);
        }else{
            // Generate a new OTP
            Integer OTP = generateOTP();
            // Save the new OTP for the user
            saveOTP(email, OTP);
            // Send the new OTP to the user's email
            sendOTPEmail(email, OTP);
        }

    }
    @Transactional
    public String reSendOTP (String email){
        Integer newOTP = generateOTP();

        Optional<OTP> userOTP = otpRepository.findByEmail(email);
        if(userOTP.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = now.plusMinutes(2);
            OTP otp = userOTP.get();
            otp.setOtpCode(newOTP);
            otp.setOtpExpiryTime(expiryTime);
            otpRepository.save(otp);
            sendOTPEmail(email, newOTP);
            return "new OTP sent.";
        }else{
            return null;
        }
    }
    @Transactional
    public void saveOTP(String email, Integer otpCode) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(2); // Set OTP expiry to 2 minutes from now

        OTP otp = OTP.builder()
                .otpCode(otpCode)
                .email(email)
                .otpExpiryTime(expiryTime)
                .build();

        otpRepository.save(otp); // Save OTP in the database

    }

    // Generate OTP method
    public Integer generateOTP() {
        User user = new User();
        String numbers = "0123456789";
        Random random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return Integer.valueOf(otp.toString());
    }

    public String verifyOTP(String email, Integer otp) {
        Optional<OTP> userOTP= otpRepository.findByEmail(email);

        if(userOTP.isPresent()){

        if (userOTP.get().getOtpCode() != null && userOTP.get().getOtpCode().equals(otp)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = userOTP.get().getOtpExpiryTime();

            // Check if OTP has expired
            if (now.isBefore(expiryTime)) {
                // OTP is valid and not expired
                return "OTP verification successful.";
            } else {
                // OTP is expired
                return "OTP is expired";
            }
        }else{return "Invalid OTP";}
        }
        else{return "Email does not exit.";}
    }

    //Login..
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        if(Objects.equals(user.getStatus(), "active")){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var jwtToken = JwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .userId(user.getId())
                    .build();
        }else{
            return null;
        }
    }

    public boolean isEmailUnique(String email) {
        return repository.findByEmail(email).isEmpty();
    }

    public boolean checkAlreadyUsers(String email) {
        Optional<User> user = repository.findByEmail(email);
        if(user.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public String getName(UUID userId) {
        Optional<User> user = repository.findById(userId);

        if(user.isPresent()){
            return  user.get().getIntName();
        }else{
            return "No name";
        }

    }
}
