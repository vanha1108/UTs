package com.example.demo.config.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ProcessingException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpStatus status;
  private String userMessage;
  private String devMessage;

  public ProcessingException(String userMessage, String devMessage) {
    super(userMessage);
    this.status = HttpStatus.OK;
    this.userMessage = userMessage;
    this.devMessage = devMessage;
  }

  public ProcessingException(HttpStatus status, String userMessage, String devMessage) {
    super(userMessage);
    this.status = status;
    this.userMessage = userMessage;
    this.devMessage = devMessage;
  }

}