package com.example.demo.request.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassRequest {

    @Min(1)
    private Long id;

    @NotBlank
    private String userName;
    private String oldPassword;

    private String newPassword;

    private String enterNewPassword;

    private String email;


}
