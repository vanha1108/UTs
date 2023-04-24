package com.example.demo.web.base;

import com.example.demo.response.PagingMeta;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VsResponseUtil {

    public static ResponseEntity<RestData<?>> ok(HttpHeaders headers) {
        return ok( HttpStatus.OK, null);
    }

    public static ResponseEntity<RestData<?>> ok( Object data) {
        return ok(HttpStatus.OK, data);
    }

    public static ResponseEntity<RestData<?>> ok(HttpStatus status, Object data) {
        RestData<?> response = new RestData<>(data);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> ok(HttpHeaders headers, HttpStatus status, Object data, PagingMeta meta) {
        RestData<?> response = new RestData<>(data, meta);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> ok(HttpHeaders headers, HttpStatus status, Object data, PagingMeta meta, Object dataUrl) {
        RestData<?> response = new RestData<>(data, meta, dataUrl);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> error(HttpHeaders headers, HttpStatus status, Object userMessage, String devMessage) {
        RestData<?> response = RestData.error(userMessage, devMessage);
        return new ResponseEntity<>(response, headers, status);
    }

    public static ResponseEntity<RestData<?>> error(HttpHeaders headers, HttpStatus status, Object userMessage) {
        RestData<?> response = RestData.error(userMessage);
        return new ResponseEntity<>(response, headers, status);
    }

    public static ResponseEntity<RestData<?>> error(HttpStatus status, Object userMessage, String devMessage) {
        RestData<?> response = RestData.error(userMessage, devMessage);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> processing(HttpStatus status, Object userMessage, String devMessage) {
        RestData<?> response = RestData.processing(userMessage, devMessage);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> error(HttpStatus status, Object object) {
        RestData<?> response = RestData.error(object);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> bad(HttpStatus status, Object userMessage, String devMessage) {
        RestData<?> response = RestData.error(userMessage, devMessage);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<RestData<?>> notFound(HttpHeaders headers, Object userMessage) {
        RestData<?> response = RestData.error(userMessage);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
