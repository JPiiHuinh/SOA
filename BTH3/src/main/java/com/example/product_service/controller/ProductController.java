package com.example.product_service.controller;

import com.example.product_service.entity.Product;
import com.example.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RestTemplate restTemplate;

    // Hàm gọi sang Bài 2 (chạy port 8080) để xác thực Token[cite: 1, 5]
    private boolean validateToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            String authUrl = "http://localhost:8080/validate";
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", token);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            ResponseEntity<Boolean> res = restTemplate.postForEntity(authUrl, entity, Boolean.class);
            return res.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(res.getBody());
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestHeader(value = "Authorization", required = false) String token) {
        if (!validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (!validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        return productService.getProductById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product, @RequestHeader(value = "Authorization", required = false) String token) {
        if (!validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product, @RequestHeader(value = "Authorization", required = false) String token) {
        if (!validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (!validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        productService.deleteProduct(id);
        return ResponseEntity.ok("Xóa thành công!");
    }
}