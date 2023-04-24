package com.example.demo.request.admin;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequest implements Input {

   @NotNull
   private Long id;

   @NotNull
   private Boolean isBlock;

}
