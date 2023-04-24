package com.example.demo.response.vehicle;

import com.example.demo.domain.dto.FeedBackDto;
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
public class VehicleDetailsResponse implements Output {
    private String owner;

    private String phone;

    private List<String> imageList;

    private String color;

    private String fuel;

    private String seat;

    private List<String> featureList;

    private Long price;

    private Long totalRun;

    private List<FeedBackDto> feedBackList;

    private String location;

    private String rule;

    private Boolean status;

    private Long id;
    private String tranmistion;
    private String vehicleName;
    private String vehicleCompany;
    private String licensePlates;

    private String vehicleType;

    private String role;

    private Boolean notDelete;

    private Long point;


}
