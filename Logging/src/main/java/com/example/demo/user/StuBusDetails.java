package com.example.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StuBusDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String route;
    private String distance;
    private Double charge;
}
