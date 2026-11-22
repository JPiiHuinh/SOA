package com.example.reporting_service.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(IllegalArgumentException ex){return ResponseEntity.badRequest().body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<?> notFound(java.util.NoSuchElementException ex){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(org.springframework.web.client.HttpStatusCodeException.class)
    public ResponseEntity<?> upstream(org.springframework.web.client.HttpStatusCodeException ex){return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error","Dịch vụ phụ thuộc trả về lỗi","details",ex.getResponseBodyAsString()));}
}
