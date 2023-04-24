package com.example.demo.service;

import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.request.comment.CommentRequest;
import com.example.demo.response.ListFeedBack;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentService {
    void comment(CommentRequest request);
    ListFeedBack feedBack(Long id);

    void editComment(CommentRequest request);
}
