package com.example.demo.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
public class RouteController {


    private final BusRouteService busService;

    @PostMapping()
    public ResponseEntity<String> register(@RequestBody BusRouteRequest busRoute) {
        try {
            busService.setRoute(busRoute);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}
