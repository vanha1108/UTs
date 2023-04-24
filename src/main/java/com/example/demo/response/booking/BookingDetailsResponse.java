package com.example.demo.response.booking;

import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.response.Output;
import com.example.demo.response.vehicle.VehicleDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsResponse implements Output {
    private VehicleDetailsResponse vehicleDetails;

    private LocalDate from;
    private LocalDate to;

    private String location;

    private Long total;

    private String name;

    private Long accountId;

    private String address;
    private String phone;
    private LocalDate bod;
    private String cardId;

    private int status;

    private String statusString;

    private String email;

    private boolean turnOnFeedBack;

    private FeedBackDto feedBack;

}
