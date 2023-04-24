package com.example.demo.request.booking;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PutBookingRequest implements Input {

    @NotNull
    private Long bookingId;

    private Boolean cancel;

    private LocalDate from;

    private LocalDate to;

}
