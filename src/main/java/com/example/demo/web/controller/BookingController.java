package com.example.demo.web.controller;

import com.example.demo.request.LoginRequest;
import com.example.demo.request.booking.BookingRequest;
import com.example.demo.request.booking.HomeBookingRequest;
import com.example.demo.response.booking.BookingResponse;
import com.example.demo.response.booking.HomeBookingResponse;
import com.example.demo.service.BookingService;
import com.example.demo.web.base.VsResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/anonymous/booking")
public class BookingController extends BaseController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("view")
    public ResponseEntity<?> bookingHome(@Valid @RequestBody HomeBookingRequest request) {
        HomeBookingResponse response = bookingService.getVehicle(request);
        return successResponse(response);
    }

    @PostMapping()
    public ResponseEntity<?> booking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.booking(request);
        return successResponse(response);
    }
    @PostMapping("sendEmail")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody BookingRequest request) {
        bookingService.sendEmail(request);
        return successResponse();
    }

    @GetMapping ("{id}")
    public ResponseEntity<?> bookingDetail(@PathVariable Long id) {
        return successResponse(bookingService.bookingDetails(id));
    }

}