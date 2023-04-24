package com.example.demo.request.booking;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest implements Input {

    @NotNull
    @Min(1)
    private Long vehicleId;

    private Long accountId;

    private String name;

    private String address;

    private String phone;

    private LocalDate bod;

    private String cardId;

    private Long total;

    private String email;

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate to;
}
