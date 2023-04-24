package com.example.demo.response.admin;

import com.example.demo.domain.dto.AccountDto;
import com.example.demo.response.Output;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListAccountResponse implements Output {
    private List<AccountDto> accounts;
}
