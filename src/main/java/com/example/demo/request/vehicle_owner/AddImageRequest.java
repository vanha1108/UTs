package com.example.demo.request.vehicle_owner;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddImageRequest implements Input {
    private String image;
    private Long accountId;
}
