package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {

    private Long id;
    private String name;
    private String location;
    private String urlImage;
    private Long price;
    private String licensePlates;
    private Boolean status;

    private Boolean notDelete;

    private boolean notOrder;

    private Long point;

    public VehicleDto(Long id, String name, String location, String urlImage, Long price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.urlImage = urlImage;
        this.price = price;
    }

    public VehicleDto(Long id, String name, String licensePlates, Boolean status,Boolean notDelete) {
        this.id = id;
        this.name = name;
        this.licensePlates = licensePlates;
        this.status = status;
        this.notDelete = notDelete;
    }
}
