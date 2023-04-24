package com.example.demo.response;


import org.springframework.http.HttpHeaders;

public interface ResponseHeader {

    public HttpHeaders getHeader();

    public HttpHeaders postHeader();

    public HttpHeaders putHeader();

    public HttpHeaders deleteHeader();

}