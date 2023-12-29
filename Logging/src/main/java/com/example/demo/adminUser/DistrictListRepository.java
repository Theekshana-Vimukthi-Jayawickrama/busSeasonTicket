package com.example.demo.adminUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DistrictListRepository extends JpaRepository<DistrictList,Integer> {
    Optional<DistrictList> findByDistrictName(String districtName);
}
