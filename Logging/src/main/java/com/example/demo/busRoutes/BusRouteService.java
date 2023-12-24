package com.example.demo.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;
    public void setRoute(BusRouteRequest request){
        BusRoute busRoute = BusRoute.builder()
                .route(request.getRoute())
                .routeNo(request.getRouteNo())
                .distance(request.getDistance())
                .charge(request.getCharge())
                .build();
        busRouteRepository.save(busRoute);
    }
}
