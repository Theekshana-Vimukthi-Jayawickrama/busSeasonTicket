package com.example.demo.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private String route;
    private String distance;
    private Double charge;
    private String routeNo;
}
