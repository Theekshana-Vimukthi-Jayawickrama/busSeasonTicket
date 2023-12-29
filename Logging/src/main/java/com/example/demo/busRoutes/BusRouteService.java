package com.example.demo.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

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
                .perDayCharge(request.getPerDayCharge())
                .build();
        busRouteRepository.save(busRoute);
    }

    public List<String> getRoute(){
        List<BusRoute> busRoutes = busRouteRepository.findAll();

        List<String> allBusRoute = new ArrayList<>();

        for(int i=0; i < busRoutes.size() ;i++){
            allBusRoute.add(busRoutes.get(i).getRoute());
        }
        return allBusRoute;
    }

    public Double calculateChargeStudent(String route){
        BusRoute busRoutes = busRouteRepository.findByRoute(route);

        if(busRoutes.getRoute().equals(route)){
            Double charge = busRoutes.getPerDayCharge();
            Double studentCharge = ((charge *40) / 100) * 30;
            return studentCharge;
        }
        return null;
    }
}
