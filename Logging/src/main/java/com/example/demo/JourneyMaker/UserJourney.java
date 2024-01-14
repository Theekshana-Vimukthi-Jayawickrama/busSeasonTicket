package com.example.demo.JourneyMaker;

import com.example.demo.Student.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_journey")
public class UserJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "journey_date")
    private LocalDate journeyDate;

    @Column(name = "journey_count")
    private int journeyCount;

    private UUID userId;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user") // Name of the foreign key column in user_journey table
    private User user;


}
