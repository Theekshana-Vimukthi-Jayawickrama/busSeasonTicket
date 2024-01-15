package com.example.demo.JourneyMaker;

import com.example.demo.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJourneyRepository extends JpaRepository<UserJourney, Integer> {
    Optional<UserJourney> findByUserIdAndJourneyDate(UUID userId, LocalDate journeyDate);
    List<UserJourney> findByUserAndDate(User user, LocalDate date, int journeyCount);
}

