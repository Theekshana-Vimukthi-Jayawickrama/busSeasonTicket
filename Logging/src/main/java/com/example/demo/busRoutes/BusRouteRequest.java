package com.example.demo.busRoutes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusRouteRequest {
    private String route;
    private String routeNo;
    private String distance;
    private Double perDayCharge;
}
