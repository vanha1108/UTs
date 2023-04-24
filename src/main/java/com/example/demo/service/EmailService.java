package com.example.demo.service;

import com.example.demo.domain.dto.EmailDetails;
import org.springframework.stereotype.Component;

@Component
public interface EmailService {

    void sendMail(EmailDetails details);
}
