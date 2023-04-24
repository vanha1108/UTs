package com.example.demo.response.booking;

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
public class HomeBookingResponse implements Output {
    private List<VehicleDto> list;

    private String background;

}
