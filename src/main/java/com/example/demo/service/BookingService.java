package com.example.demo.service;

import com.example.demo.request.booking.BookingRequest;
import com.example.demo.request.booking.BookingsRequest;
import com.example.demo.request.booking.HomeBookingRequest;
import com.example.demo.request.booking.PutBookingRequest;
import com.example.demo.response.booking.BookingDetailsResponse;
import com.example.demo.response.booking.BookingResponse;
import com.example.demo.response.booking.HomeBookingResponse;
import com.example.demo.response.booking.UserBookingsResponse;
import org.springframework.stereotype.Component;

@Component
public interface BookingService {

    HomeBookingResponse getVehicle(HomeBookingRequest request);

    BookingResponse booking(BookingRequest request);
    void sendEmail(BookingRequest request);

    BookingDetailsResponse bookingDetails(Long id);

    UserBookingsResponse userBookings(BookingsRequest request);

    void putBooking(PutBookingRequest request);
}
