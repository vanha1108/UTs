package com.example.demo.response.vehicle;

import com.example.demo.domain.dto.VehicleDetailDto;
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
    private List<VehicleDetailDto> list;
}