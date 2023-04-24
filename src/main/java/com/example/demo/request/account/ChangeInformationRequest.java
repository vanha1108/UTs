package com.example.demo.request.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeInformationRequest {

    @Min(1)
    private long id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;


    private String name;

    private String phone;

    private String address;

    private String cardId;

    private String gender;

    private LocalDate bod;

}
