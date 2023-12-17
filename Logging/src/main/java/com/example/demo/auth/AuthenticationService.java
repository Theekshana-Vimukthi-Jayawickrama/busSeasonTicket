package com.example.demo.auth;

import com.example.demo.OTPGenerator.OTP;
import com.example.demo.OTPGenerator.OTPRepository;
import com.example.demo.busRoutes.BusRoute;
import com.example.demo.busRoutes.BusRouteRepository;
import com.example.demo.config.JwtService;
import com.example.demo.demo.EmailAlreadyExistsException;
import com.example.demo.demo.UserService;
import com.example.demo.user.*;
import com.example.demo.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

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
    public AuthenticationResponse register(RegisterRequest request, SchoolRequest stuRequest, GuardianRequest guardianRequest,RouteRequest routeRequest, MultipartFile birthFile, MultipartFile approvalLetter) throws Exception {

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
        StuBusDetails stuBusDetails = stuBusRoute(route);

       var user = User.builder()
                .fullname(request.getFullname())
                .intName(request.getIntName())
                .email(email)
                .stuBusDetails(stuBusDetails)
                .schoolDetails(schoolDetails)
                .guardianDetails(guardianDetails)
                .gender(request.getGender())
                .dob(request.getDob())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

       //BirthFile handle functions
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(birthFile.getOriginalFilename()));

//        try {
//            if (fileName.contains("..")) {
//                throw new Exception("The file name is invalid" + fileName);
//            }
//            StudentBirthFiles studentBirthFiles = studentBirthFilesRepo.save(StudentBirthFiles.builder()
//                    .fileBirthName(birthFile.getOriginalFilename())
//                    .fileBirthType(birthFile.getContentType())
//                    .fileBirthData(ImageUtils.compressImage(birthFile.getBytes()))
//                    .build());
//            user.setStudentBirthFiles(studentBirthFiles);
//
//        } catch (Exception e) {
//            throw new Exception("File could not be saved");
//        }
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

        repository.save(user);
        var jwtToken = JwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //Student PDF letter
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

    public byte[] downloadImage(Long fileId){
        Optional<StudentBirthFiles> dbImageData = uploadRepository.findById(fileId);
        return ImageUtils.decompressImage(dbImageData.get().getFileBirthData());
    }

    //BusRoute that student will go...
    public StuBusDetails stuBusRoute(String route){
        BusRoute busRoute = busRouteRepository.findByRoute(route);
        try {
            if (busRoute != null) {
                Double charge = busRoute.getCharge();
                String distance = busRoute.getDistance();

                return StuBusDetails.builder()
                        .charge(charge)
                        .distance(distance)
                        .route(route)
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
        public void sendOTPEmail(String toEmail, String otp) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("OTP Verification");
            message.setText("Your OTP for verification is: " + otp);
            emailSender.send(message);
        }

    public void resendOTP(String email) {
        Optional<User> userOptional = repository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Generate a new OTP
            String newOTP = generateOTP();
            // Save the new OTP for the user
            saveOTP(user, newOTP);
            // Send the new OTP to the user's email
            sendOTPEmail(email, newOTP);
        } else {
            // Handle cases where user email doesn't exist
            throw new IllegalArgumentException("User with provided email doesn't exist");
        }
    }

    public void saveOTP(User user, String otpCode) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(5); // Set OTP expiry to 5 minutes from now

        OTP otp = OTP.builder()
                .otpCode(otpCode)
                .otpExpiryTime(expiryTime)
                .build();

        user.setOtp(otp); // Associate OTP with the user
        otpRepository.save(otp); // Save OTP in the database

        repository.save(user); // Update user with OTP details
    }

    // Generate OTP method
    public String generateOTP() {
        User user = new User();
        String numbers = "0123456789";
        Random random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        // Set OTP expiration time (e.g., 5 minutes from now)
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // Change 5 to your desired expiry duration
        return otp.toString();
    }

    public String verifyOTP(String email, String otp) {
        User user = repository.findByEmail(email)
                .orElseThrow(); // Fetch user by email (consider error handling)

        OTP userOTP = user.getOtp();
        if(user.getEmail().equals(email)){
        if (userOTP != null && userOTP.getOtpCode().equals(otp)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = userOTP.getOtpExpiryTime();

            // Check if OTP has expired
            if (now.isBefore(expiryTime)) {
                // OTP is valid and not expired
                return "OTP verification successful.";
            } else {
                // OTP is expired
                return "OTP is expired";
            }
        }else{
            // Invalid OTP
            return "Invalid OTP";
        }}else{
            return "Email does not exit";
            }
    }


    //Login..
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = JwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public boolean isEmailUnique(String email) {
        return repository.findByEmail(email).isEmpty();
    }


}
