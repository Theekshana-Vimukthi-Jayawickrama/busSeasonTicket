package com.example.demo.JourneyMaker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UserJourneyRepository extends JpaRepository<UserJourney, Integer> {
    Optional<UserJourney> findByUserIdAndJourneyDate(UUID userId, LocalDate journeyDate);
}

