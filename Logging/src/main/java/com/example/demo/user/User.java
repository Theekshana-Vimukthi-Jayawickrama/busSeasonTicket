package com.example.demo.user;

import com.example.demo.JourneyMaker.UserJourney;
import com.example.demo.OTPGenerator.OTP;
import com.example.demo.QRcode.QRCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String fullname;
    private String intName;
    private String dob;
    private String address;
    private String gender;
    private Boolean verified;
    private String telephoneNumber;
    private String residence;
    private String status;

    @Column(unique = true)
    private  String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserJourney> journeys = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_otp", joinColumns = {@JoinColumn(name = "fk_stu")},
    inverseJoinColumns = {@JoinColumn(name = "fk_otp")})
    private OTP otp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_QR", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_QR")})
    private QRCode qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_bus", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_bus")})
    private StuBusDetails stuBusDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_school_id")
    private SchoolDetails schoolDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_birth_id")
    private StudentBirthFiles studentBirthFiles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_photo_id")
    private StudentPhotos userPhoto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_approval_letter")
    private ApprovalLetter approvalLetter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_guardian")
    private  GuardianDetails guardianDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
