package com.example.demo.domain.dto;

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
public class AccountDto {
    private Long id;
    private String name;
    private String userName;

    private String email;

    private String role;

    private Boolean isBlock;

    private String address;
    private LocalDate bod;
    private String phone;
    private String gender;
    private Long point;

    private String cardId;

    private List<FeedBackDto> feedBacks;
}
