package com.example.demo.request.comment;

import com.example.demo.request.Input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest implements Input {
    @NotNull
    private Long accountId;
    @NotNull
    private Long bookingId;

    @Min(0L)
    @Max(5L)
    private Long point;

    private String comment;
}
