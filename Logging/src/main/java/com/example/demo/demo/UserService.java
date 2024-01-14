package com.example.demo.demo;

import com.example.demo.Student.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


}
