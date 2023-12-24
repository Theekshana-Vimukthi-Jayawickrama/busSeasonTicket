package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentBirthFilesRepo extends JpaRepository<StudentBirthFiles, Long> {
Optional<StudentBirthFiles> findById(Long id);
}
