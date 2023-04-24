package com.example.demo.response.vehicle_owner;

import com.example.demo.domain.dto.VehicleDto;
import com.example.demo.response.Output;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetVehiclesResponse implements Output {
    private List<VehicleDto> listCar;
    private List<VehicleDto> listMotorbike;

    private List<VehicleDto> listBicycle;


}
