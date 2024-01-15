package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalLetterRepository extends JpaRepository<ApprovalLetter,Integer> {
    Optional<ApprovalLetter> findById(Integer id);
}
