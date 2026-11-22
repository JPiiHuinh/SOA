package com.example.reporting_service.controller;

import com.example.reporting_service.dto.*;
import com.example.reporting_service.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportingService reports;
    private final IntegrationService integration;
    public ReportController(ReportingService reports,IntegrationService integration){this.reports=reports;this.integration=integration;}
    private ResponseEntity<?> unauthorized(String token){return integration.isTokenValid(token)?null:ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");}

    @GetMapping("/products")
    public ResponseEntity<?> products(@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(reports.getProductReports(token));}
    @GetMapping("/products/{id}")
    public ResponseEntity<?> product(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(reports.getProductReport(id,token));}
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductReportRequest body,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.status(HttpStatus.CREATED).body(reports.createProductReport(body,token));}
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);if(e!=null)return e;reports.deleteProductReport(id);return ResponseEntity.noContent().build();}

    @GetMapping("/orders")
    public ResponseEntity<?> orders(@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(reports.getOrderReports());}
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> order(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(reports.getOrderReport(id));}
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderReportRequest body,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.status(HttpStatus.CREATED).body(reports.createOrderReport(body,token));}
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);if(e!=null)return e;reports.deleteOrderReport(id);return ResponseEntity.noContent().build();}
}
