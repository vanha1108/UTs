package com.example.demo.response.admin;

import com.example.demo.response.Output;
import com.example.demo.response.vehicle.VehicleDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListVehicleResponse implements Output {
    private List<VehicleDetailsResponse> vehicleCars;

    private List<VehicleDetailsResponse> vehicleMotorbikes;

    private List<VehicleDetailsResponse> vehicleBicycles;
}
