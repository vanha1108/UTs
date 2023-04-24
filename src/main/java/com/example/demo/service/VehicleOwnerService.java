package com.example.demo.service;

import com.example.demo.domain.model.Vehicle;
import com.example.demo.request.vehicle_owner.CreateVehicleRequest;
import com.example.demo.request.vehicle_owner.EditBookingRequest;
import com.example.demo.request.vehicle_owner.GetVehiclesRequest;
import com.example.demo.request.vehicle_owner.OwnerBookingRequest;
import com.example.demo.response.booking.BookingDetailsResponse;
import com.example.demo.response.vehicle.VehicleDetailsResponse;
import com.example.demo.response.vehicle_owner.GetVehiclesResponse;
import com.example.demo.response.vehicle_owner.OwnerBookingResponse;
import org.springframework.stereotype.Component;

@Component
public interface VehicleOwnerService {

    OwnerBookingResponse listBooking(OwnerBookingRequest request);

    void editBooking(EditBookingRequest request);

    GetVehiclesResponse getVehicles(GetVehiclesRequest request);

    void createVehicle(CreateVehicleRequest request);

    void deleteVehicle(Long id);

    void editVehicle(Long id, CreateVehicleRequest request);

    void addImage(Long id, String image);

    VehicleDetailsResponse detailVehicle(Long id);
}
