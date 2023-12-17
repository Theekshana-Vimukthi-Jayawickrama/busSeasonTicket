package com.example.demo.busRoutes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRouteRepository extends JpaRepository<BusRoute,Integer> {

    BusRoute findByRoute(String route);
}
