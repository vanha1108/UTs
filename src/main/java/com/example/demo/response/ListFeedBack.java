package com.example.demo.response;

import com.example.demo.domain.dto.FeedBackDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListFeedBack implements Output{
    List<FeedBackDto> list;
}
