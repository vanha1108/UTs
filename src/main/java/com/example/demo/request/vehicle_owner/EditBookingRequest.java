package com.example.demo.request.vehicle_owner;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditBookingRequest implements Input {

   private String cardId;
   private String name;

   private String phone;
   private LocalDate from;
   private LocalDate to;
   private Long total;

   private Long bookingId;

   private LocalDate bod;

   private Integer isAccept;

   private String address;

}
