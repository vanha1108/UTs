package com.example.demo.request.admin;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListVehicleRequest implements Input {

   private String name;
   private Integer status;

   private Integer role;
}
