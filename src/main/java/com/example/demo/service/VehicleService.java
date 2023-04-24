package com.example.demo.service;

import com.example.demo.request.admin.ConfirmVehicleRequest;
import com.example.demo.request.admin.ListVehicleRequest;
import com.example.demo.response.admin.ListVehicleResponse;
import com.example.demo.response.vehicle.VehicleDetailsResponse;
import com.example.demo.response.vehicle.GetVehiclesResponse;
import org.springframework.stereotype.Component;

@Component
public interface VehicleService {

    GetVehiclesResponse getVehicleType();

    GetVehiclesResponse getVehicleCompany();
    GetVehiclesResponse getTranmistionType();
    GetVehiclesResponse getSeatType();
    GetVehiclesResponse getFuelType();

    GetVehiclesResponse getColorType();

    GetVehiclesResponse getShop(String location);

    VehicleDetailsResponse getVehicle(Long id);

    ListVehicleResponse getList(ListVehicleRequest request);

    void confirmVehicle(ConfirmVehicleRequest request);

}
