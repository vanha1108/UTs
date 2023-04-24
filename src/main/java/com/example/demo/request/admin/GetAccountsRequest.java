package com.example.demo.request.admin;

import com.example.demo.domain.model.ERole;
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
public class GetAccountsRequest implements Input {

   private String role;

   private Boolean isBlock;

   private String keyword;

}
