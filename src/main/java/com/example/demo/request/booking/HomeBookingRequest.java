package com.example.demo.request.booking;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeBookingRequest implements Input {
    @Size(max = 100)
    private String location;

    private Long vehicleType;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    private Boolean orderPrice;

    private Long seatType;

    private Long vehicleCompany;

    private Long fuel;

    private Long accountId;

    private Long minPrice;

    private Long maxPrice;

    private Long tranmistion;

    private Long star;

    private String name;


}
