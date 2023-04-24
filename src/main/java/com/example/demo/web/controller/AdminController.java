package com.example.demo.web.controller;

import com.example.demo.request.admin.ChangeStatusRequest;
import com.example.demo.request.admin.ConfirmVehicleRequest;
import com.example.demo.request.admin.GetAccountsRequest;
import com.example.demo.request.admin.ListVehicleRequest;
import com.example.demo.service.VehicleService;
import com.example.demo.service.impl.AccountDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController extends BaseController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private AccountDetailsServiceImpl accountDetailsService;


    @PostMapping("/vehicle-info")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> vehicleInfo(@Valid @RequestBody ListVehicleRequest request) {
        return successResponse(vehicleService.getList(request));
    }

    @PostMapping("/vehicle-info/confirm")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> vehicleInfo(@Valid @RequestBody ConfirmVehicleRequest request) {
        vehicleService.confirmVehicle(request);
        return successResponse();
    }

    @PostMapping("/account-info")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> accountInfo(@Valid @RequestBody GetAccountsRequest request) {
        return successResponse(accountDetailsService.getListAccount(request));
    }

    @PutMapping("/change-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody ChangeStatusRequest request) {
        accountDetailsService.changeStatus(request);
        return successResponse();
    }


}