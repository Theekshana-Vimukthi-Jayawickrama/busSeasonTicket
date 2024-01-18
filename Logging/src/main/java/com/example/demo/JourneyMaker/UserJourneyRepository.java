package com.example.demo.JourneyMaker;

import com.example.demo.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface UserJourneyRepository extends JpaRepository<UserJourney, Integer> {
    Optional<UserJourney> findByUserIdAndJourneyDate(UUID userId, LocalDate journeyDate);
    List<UserJourney> findByUserAndJourneyDate(User user, LocalDate journeyDate);

}

