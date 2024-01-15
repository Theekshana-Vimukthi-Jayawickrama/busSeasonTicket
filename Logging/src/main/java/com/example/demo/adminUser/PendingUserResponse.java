package com.example.demo.adminUser;

import com.example.demo.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingUserResponse {
    private String userName;
    private UUID id;
    private String status;
    private Role role;
}
