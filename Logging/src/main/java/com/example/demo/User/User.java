package com.example.demo.User;

import com.example.demo.JourneyMaker.SelectDays;
import com.example.demo.JourneyMaker.UserJourney;
import com.example.demo.OTPGenerator.OTP;
import com.example.demo.QRcode.QRCode;
import com.example.demo.Subscription.UserSubscription;
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
    private String fullName;
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
    @Column(length = 50)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserJourney> journeys = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_otp", joinColumns = {@JoinColumn(name = "fk_stu")},
    inverseJoinColumns = {@JoinColumn(name = "fk_otp")})
    private OTP otp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="user_selectDay", joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_selectDays")})
    private SelectDays selectDays;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_QR", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_QR")})
    private QRCode qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_bus", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_bus")})
    private StuBusDetails stuBusDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_subscription", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subscription")})
    private UserSubscription studentSubscription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_school_id")
    private SchoolDetails schoolDetails;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_birthFile", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_BirthFile")})
    private StudentBirthFiles studentBirthFiles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="User_userPhoto", joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_userPhoto")})
    private UserPhotos userPhoto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="Adult_userNICPhoto", joinColumns = {@JoinColumn(name = "fk_Adult")},
            inverseJoinColumns = {@JoinColumn(name = "fk_userNICPhoto")})
    private AdultNIC adultNIC;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="student_approvalLetter", joinColumns = {@JoinColumn(name = "fk_student")},
            inverseJoinColumns = {@JoinColumn(name = "fk_approvalLetter")})
    private ApprovalLetter approvalLetter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_guardianDetails", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_guardianDetails")})
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
