package com.example.demo.busRoutes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRouteRepository extends JpaRepository<BusRoute,Integer> {

    BusRoute findByRoute(String route);
    List<BusRoute> findAll();
}
