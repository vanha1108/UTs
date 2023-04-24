package com.example.demo.repository;

import com.example.demo.domain.model.TranmistionType;
import com.example.demo.domain.model.Vehicle;
import com.example.demo.request.booking.HomeBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranmistionTypeRepository extends JpaRepository<TranmistionType, Long> {

}
