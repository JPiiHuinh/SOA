package com.example.order_service.controller;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.service.IntegrationService;
import com.example.order_service.service.OrderService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService service;
    private final IntegrationService integration;
    public OrderController(OrderService service,IntegrationService integration){this.service=service;this.integration=integration;}

    private ResponseEntity<?> unauthorized(String token){return integration.validateToken(token)?null:ResponseEntity.status(401).body("Token không hợp lệ!");}

    @GetMapping("/orders")
    public ResponseEntity<?> all(@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(service.findAll());}
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> one(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(service.findById(id));}
    @PostMapping("/orders")
    public ResponseEntity<?> create(@RequestBody CreateOrderRequest body,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.status(201).body(service.create(body,token));}
    @PutMapping("/orders/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody java.util.Map<String,String> body,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(service.updateStatus(id,body.get("status")));}
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);if(e!=null)return e;service.delete(id);return ResponseEntity.ok("Xóa đơn hàng thành công");}

    @GetMapping("/order_items")
    public ResponseEntity<?> allItems(@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(service.findAllItems());}
    @GetMapping("/order_items/{id}")
    public ResponseEntity<?> oneItem(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(service.findItem(id));}
    @PostMapping("/order_items")
    public ResponseEntity<?> addItem(@RequestBody OrderItemRequest body,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.status(201).body(service.addItem(body.getOrderId(),body.getProductId(),body.getQuantity(),token));}
    @PutMapping("/order_items/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id,@RequestBody OrderItemRequest body,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);return e!=null?e:ResponseEntity.ok(service.updateItem(id,body.getProductId(),body.getQuantity(),token));}
    @DeleteMapping("/order_items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){var e=unauthorized(token);if(e!=null)return e;service.deleteItem(id);return ResponseEntity.ok("Xóa mặt hàng thành công");}
}