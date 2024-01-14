package com.example.demo.Subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSubscriptionRepo extends JpaRepository<StudentSubscription,Integer> {
    Optional<StudentSubscription> findById(int id);
}
