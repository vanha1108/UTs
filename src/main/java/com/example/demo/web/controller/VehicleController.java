package com.example.demo.web.controller;

import com.example.demo.request.booking.GetShopRequest;
import com.example.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/anonymous/vehicle")
public class VehicleController extends BaseController{

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("type")
    public ResponseEntity<?> getVehicleType() {

        return successResponse(vehicleService.getVehicleType());
    }

    @GetMapping("company")
    public ResponseEntity<?> getVehicleCompany() {

        return successResponse(vehicleService.getVehicleCompany());
    }

    @GetMapping("tranmistion")
    public ResponseEntity<?> getTranmistionType() {

        return successResponse(vehicleService.getTranmistionType());
    }

    @GetMapping("seat")
    public ResponseEntity<?> getSeatType() {

        return successResponse(vehicleService.getSeatType());
    }

    @GetMapping("fuel")
    public ResponseEntity<?> getFuelType() {

        return successResponse(vehicleService.getFuelType());
    }

    @GetMapping("color")
    public ResponseEntity<?> getColorType() {

        return successResponse(vehicleService.getColorType());
    }


    @PostMapping("shop")
    public ResponseEntity<?> getShop(@RequestBody GetShopRequest request) {
        return successResponse(vehicleService.getShop(request.getAddress()));
    }

    @GetMapping("details/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable Long id) {

        return successResponse(vehicleService.getVehicle(id));
    }



}