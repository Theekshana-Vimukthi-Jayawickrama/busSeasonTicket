package com.example.demo.auth;

import com.example.demo.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullname;
    private String intName;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String address;
    private Role role;

}
