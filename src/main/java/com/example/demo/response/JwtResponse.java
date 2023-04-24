package com.example.demo.response;

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
public class JwtResponse {
    private String jwt;
    private Long id;
    private String userName;
    private String Email;
    private List<String> roles;

    private String phone;

    private String address;
    private LocalDate bod;
    private String cardId;
    private String name;
    private Long point;

    private String gender;

    private String image;
}
