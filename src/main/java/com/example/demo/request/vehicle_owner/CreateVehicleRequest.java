package com.example.demo.request.vehicle_owner;

import com.example.demo.domain.model.Feature;
import com.example.demo.domain.model.Image;
import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequest implements Input {
    private String name;
    private Long accountId;

    private Long type;
    private Long color;

    private Long fuel;

    private Long seat;

    private Long tranmistion;
    private Long vehicleCompany;
    private Long price;
    private String location;
    private List<String> imageList;
    private List<String> featureList;

    private String rule;

    private String licensePlates;
}
