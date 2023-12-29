package com.example.demo.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/routeList")
    public ResponseEntity<List<String>> getList() {
       try {
           List<String> allBusRoutes = busService.getRoute();
           return ResponseEntity.ok(allBusRoutes);
       }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.notFound().build();
       }
    }

    @GetMapping("/getStudentCharge/{route}")
    public ResponseEntity<Double> getStudentCharge(@PathVariable String route){
        try{
            Double charge = busService.calculateChargeStudent(route);
            return  ResponseEntity.ok(charge);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
