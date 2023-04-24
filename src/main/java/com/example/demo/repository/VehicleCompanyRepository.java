package com.example.demo.repository;

import com.example.demo.domain.model.VehicleCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleCompanyRepository extends JpaRepository<VehicleCompany, Long> {

}
