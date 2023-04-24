package com.example.demo.web.controller;

import com.example.demo.request.booking.BookingRequest;
import com.example.demo.request.booking.HomeBookingRequest;
import com.example.demo.request.vehicle_owner.*;
import com.example.demo.response.booking.BookingResponse;
import com.example.demo.response.booking.HomeBookingResponse;
import com.example.demo.service.BookingService;
import com.example.demo.service.VehicleOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vehicle-owner")
public class VehicleOwnerController extends BaseController {

    @Autowired
    private VehicleOwnerService vehicleOwnerService;


    @PostMapping("/booking")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> bookingDetail(@Valid @RequestBody OwnerBookingRequest request) {
        return successResponse(vehicleOwnerService.listBooking(request));
    }

    @PutMapping("/booking/edit")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> editBooking(@Valid @RequestBody EditBookingRequest request) {
        vehicleOwnerService.editBooking(request);
        return successResponse();
    }

    @PostMapping("/list")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> getVehicle(@Valid @RequestBody GetVehiclesRequest request) {
        return successResponse(vehicleOwnerService.getVehicles(request));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        vehicleOwnerService.createVehicle(request);
        return successResponse();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        vehicleOwnerService.deleteVehicle(id);
        return successResponse();
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> editVehicle(@PathVariable Long id, @Valid @RequestBody CreateVehicleRequest request) {
        vehicleOwnerService.editVehicle(id, request);
        return successResponse();
    }

    @PutMapping("/addImage")
    @PreAuthorize("hasRole('ROLE_SHOP')")
    public ResponseEntity<?> editVehicle(@Valid @RequestBody AddImageRequest request) {
        vehicleOwnerService.addImage(request.getAccountId(), request.getImage());
        return successResponse();
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP')")
    public ResponseEntity<?> detailVehicle(@PathVariable Long id) {

        return successResponse(vehicleOwnerService.detailVehicle(id));
    }

}