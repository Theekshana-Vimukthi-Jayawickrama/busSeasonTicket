package com.example.demo.busRoutes;

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
public class BusRoute {

    @Id
    @GeneratedValue
    private Integer id;
    private String route;
    private Integer routeNo;
    private String distance;
    private Double charge;


}
