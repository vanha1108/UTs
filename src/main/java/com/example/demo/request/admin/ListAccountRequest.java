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
public class ListAccountRequest implements Input {

   private Long roleId;

   private Boolean status;

   private String keyword;

}
